/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philihp.bj;

import com.philihp.bj.players.ThorpeBasicPlayerWithMisChance;
import com.philihp.bj.players.DealerPlayer;
import com.philihp.bj.players.Player;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lorenz
 */
public class Simulator {

    /**
     * Number of simulations that are runned
     */
    public static int NUMBER_OF_SIMULATIONS = 100000;

    /**
     * Number of hands that are played in a simulation
     */
    public static int NUMBER_OF_HANDS = 100;

    /**
     * Chance player chooses wrong response
     */
    public static double CHANCE_OF_WRONG_RESPONSE = 0.075;

    /**
     * Number of decks in a shoe
     */
    public static int SHOE_SIZE = 6;

    /**
     * Dealer Hits or Stands on a soft 17
     */
    public static Response SOFT17 = Response.H;

    /**
     * Blackjack Payout
     */
    public static float BLACKJACK_PAYOUT = 3f / 2f;

    /**
     * Can Double-Down after Split?
     */
    public static boolean DOUBLE_AFTER_SPLIT = true;

    /**
     * Amount of penetration into the shoe. 0.75 would mean to play 75% of the
     * shoe.
     */
    public static float CUT_CARD_PENETRATION = 0.66667f;

    public static int MIN_BET = 5;

    /**
     * House limit on number of resplits (max this many hands)
     */
    public static int LIMIT_ON_RESPLITS = 4;

    public static SecureRandom randomizer;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        randomizer = new SecureRandom();

        double[] mischances = {0.0, 0.01, 0.02, 0.03, 0.05, 0.075, 0.1, 0.125, 0.15, 0.175, 0.2, 0.25, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};

        for (int i = 0; i < mischances.length; i++) {
            Player player = new ThorpeBasicPlayerWithMisChance(mischances[i]);
            Player dealer = new DealerPlayer();

            long money = 0;
            long handsPlayed = 0;
            long simulationsRunned = 0;

            List<SimulationRunResult> resultList = new ArrayList<>();

            while (simulationsRunned < NUMBER_OF_SIMULATIONS) {
                Deck deck = new Deck(SHOE_SIZE, player);
                deck.shuffle(randomizer);
                player.resetCount(SHOE_SIZE);

                try {
                    player.setNumberOfMistakes(0);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }

                simulationsRunned++;
                while (handsPlayed < NUMBER_OF_HANDS) {
                    handsPlayed++;
                    if ((float) deck.size() / deck.getInitialSize() > CUT_CARD_PENETRATION) {
                        deck = new Deck(SHOE_SIZE, player);
                        deck.shuffle(randomizer);
                        player.resetCount(SHOE_SIZE);
                    }

                    List<Hand> playerHands = new ArrayList<>(1);
                    playerHands.add(new Hand(player.bet(), deck.draw(), deck.draw(), false));
                    Hand dealerHand = new Hand(deck.draw(), deck.draw());
                    money -= playerHands.get(0).getBet();

                    if (dealerHand.getValue() == 21) {
                    //dealer has blackjack. do not play out hands, just leave money on
                        //table and start over.
                        continue;
                    }

                    money += playoutPlayer(player, deck, playerHands, dealerHand);
                    playoutDealer(deck, dealer, dealerHand);

                    for (Hand playerHand : playerHands) {
                        money += payout(playerHand, dealerHand);
                    }
                }

                double houseEdge = -(100 * (double) money / (handsPlayed * MIN_BET));

//            System.out.println("Simulation " + simulationsRunned + ":");
//            System.out.println("Hands Played:    " + handsPlayed);
//            System.out.println("Money:           " + money);
//            System.out.println("Min-Bet:         " + MIN_BET);
//            System.out.println("House Edge :     " + houseEdge + "%");
//            System.out.println("Mistakes:        " + player.getNumberOfMistakes());
//            System.out.println();
                resultList.add(new SimulationRunResult(simulationsRunned, money, houseEdge, player.getNumberOfMistakes()));

                handsPlayed = 0;
                money = 0;
            }

            double avgPayout = resultList.stream().mapToLong(SimulationRunResult::getPayout).average().getAsDouble();
            double avgHouseEdge = resultList.stream().mapToDouble(SimulationRunResult::getHouseEdge).average().getAsDouble();
            double avgMistakes = resultList.stream().mapToInt(SimulationRunResult::getNumberOfMistakes).average().getAsDouble();
            System.out.println("Average payout: " + avgPayout);
            System.out.println("Average House Edge: " + avgHouseEdge + "%");
            System.out.println("Averages mistakes: " + avgMistakes);

            System.out.println("Runtime: " + ((float) (System.nanoTime() - startTime) / 1000000000f) + " seconds");

            String path = "F:\\result " + NUMBER_OF_SIMULATIONS + "simulations " + mischances[i] + "Mischance.csv";
            writeCSV(path, resultList);
        }
    }

    private static float playoutPlayer(Player player, Deck deck,
            List<Hand> playerHands, Hand dealerHand) {
        int i = 0;
        float money = 0;
        do {
            Hand playerHand = playerHands.get(i);

            for (;;) {
                Response response = player.prompt(playerHand, dealerHand, playerHands.size() < LIMIT_ON_RESPLITS);
                if (response == Response.RH) {
                    if (playerHand.size() == 2 && playerHand.isSplit() == false) {
                        playerHand.surrender();
                        break;
                    } else {
                        response = Response.H;
                    }
                }
                if (response == Response.DH) {
                    if (playerHand.canDoubleDown()) {
                        int bet = playerHand.getBet();
                        money -= bet;
                        playerHand.addBet(bet);
                        playerHand.add(deck.draw());
                        break;
                    } else {
                        response = Response.H;
                    }
                }
                if (response == Response.H) {
                    playerHand.add(deck.draw());
                    if (playerHand.getValue() > 21) // bust!
                    {
                        break;
                    } else {
                        continue;
                    }
                }
                if (response == Response.DS) {
                    if (playerHand.canDoubleDown()) {
                        int bet = playerHand.getBet();
                        money -= bet;
                        playerHand.addBet(bet);
                        response = Response.S;
                    } else {
                        response = Response.S;
                    }
                }
                if (response == Response.S) {
                    break;
                }
                if (response == Response.P) {
                    money -= playerHand.getBet();
                    Hand left = new Hand(playerHand.getBet(), playerHand.get(0), deck.draw(), true);
                    Hand right = new Hand(playerHand.getBet(), playerHand.get(1), deck.draw(), true);
                    playerHands.set(i, left);
                    playerHands.add(right);
                    playerHand = left;
                    continue;
                }
            }
        } while (++i < playerHands.size());
        return money;
    }

    private static void playoutDealer(Deck deck, Player dealer, Hand dealerHand) {
        do {
            Response response = dealer.prompt(null, dealerHand, false);
            if (response == Response.H) {
                dealerHand.add(deck.draw());
            } else if (response == Response.S) {
                break;
            } else {
                throw new RuntimeException("Dealer should never do anything but Hit or Stay. " + dealerHand);
            }
        } while (dealerHand.getValue() <= 21); // bust
    }

    private static int payout(Hand playerHand,
            Hand dealerHand) {
        if (playerHand.isSurrendered()) {
            return playerHand.getBet() / 2;
        } else if (playerHand.isBlackjack() && dealerHand.isBlackjack()) {
            //System.out.println("--- \tPush, both blackjacks");
            return playerHand.getBet();
        } else if (playerHand.isBlackjack() && dealerHand.isBlackjack() == false) {
            //System.out.println("Win \tPlayer BJ "+playerHand);
            return (int) (playerHand.getBet() * (1 + BLACKJACK_PAYOUT));
        } else if (playerHand.isBlackjack() == false && dealerHand.isBlackjack()) {
            //System.out.println("Lose \tDealer BJ "+dealerHand);
            return 0;
        } else if (playerHand.getValue() > 21) {
            //System.out.println("Lose \tPlayer Bust "+playerHand.getValue());
            return 0;
        } else if (dealerHand.getValue() > 21) {
            //System.out.println("Win \tDealer Bust "+dealerHand.getValue());
            return playerHand.getBet() * 2;
        } else if (playerHand.getValue() > dealerHand.getValue()) {
            //System.out.println("Win \tplayer="+playerHand.getValue()+", dealer="+dealerHand.getValue());
            return playerHand.getBet() * 2;
        } else if (playerHand.getValue() < dealerHand.getValue()) {
            //System.out.println("Lose \tplayer="+playerHand.getValue()+", dealer="+dealerHand.getValue());
            return 0;
        } else if (playerHand.getValue() == dealerHand.getValue()) {
            //System.out.println("--- \tPush @ " + dealerHand.getValue());
            return playerHand.getBet();
        } else {
            throw new RuntimeException("Player: " + playerHand + ", Dealer: " + dealerHand);
        }
    }

    private static void writeCSV(String fileName, List<SimulationRunResult> list) {
        try {
            FileWriter writer = new FileWriter(fileName);

            writer.append("Simulation number");
            writer.append(';');
            writer.append("Hands Played");
            writer.append(';');
            writer.append("Minimum Bet");
            writer.append(';');
            writer.append("Payout");
            writer.append(';');
            writer.append("House Edge (%)");
            writer.append(';');
            writer.append("Number of Mistakes");
            writer.append('\n');

            for (SimulationRunResult result : list) {
                writer.append(String.valueOf(result.getSimulationNr()));
                writer.append(';');
                writer.append(String.valueOf(NUMBER_OF_HANDS));
                writer.append(';');
                writer.append(String.valueOf(MIN_BET));
                writer.append(';');
                writer.append(String.valueOf(result.getPayout()));
                writer.append(';');
                writer.append(String.valueOf(result.getHouseEdge()));
                writer.append(';');
                writer.append(String.valueOf(result.getNumberOfMistakes()));
                writer.append('\n');
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
