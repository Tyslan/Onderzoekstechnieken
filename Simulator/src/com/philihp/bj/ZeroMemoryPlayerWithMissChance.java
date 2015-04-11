/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philihp.bj;

import static com.philihp.bj.Response.DH;
import static com.philihp.bj.Response.DS;
import static com.philihp.bj.Response.H;
import static com.philihp.bj.Response.P;
import static com.philihp.bj.Response.RH;
import static com.philihp.bj.Response.S;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lorenz
 */
public class ZeroMemoryPlayerWithMissChance extends ZeroMemoryPlayer {

    private static final SecureRandom random = new SecureRandom();
    private double chanceWrongResponse;
    private int numberOfMistakes;

    private static final Response[][] HARD_RESPONSE = {
        //2  3  4  5  6  7  8  9 10  A
        {H, H, H, H, H, H, H, H, H, H}, //4
        {H, H, H, H, H, H, H, H, H, H}, //5
        {H, H, H, H, H, H, H, H, H, H}, //6
        {H, H, H, H, H, H, H, H, H, H}, //7
        {H, H, H, H, H, H, H, H, H, H}, //8
        {H, DH, DH, DH, DH, H, H, H, H, H}, //9
        {DH, DH, DH, DH, DH, DH, DH, DH, H, H}, //10
        {DH, DH, DH, DH, DH, DH, DH, DH, DH, H}, //11 last could be DH?
        {H, H, S, S, S, H, H, H, H, H}, //12
        {S, S, S, S, S, H, H, H, H, H}, //13
        {S, S, S, S, S, H, H, H, H, H}, //14
        {S, S, S, S, S, H, H, H, RH, H}, //15
        {S, S, S, S, S, H, H, RH, RH, RH}, //16
        {S, S, S, S, S, S, S, S, S, S}, //17
        {S, S, S, S, S, S, S, S, S, S}, //18
        {S, S, S, S, S, S, S, S, S, S}, //19
        {S, S, S, S, S, S, S, S, S, S}, //20
        {S, S, S, S, S, S, S, S, S, S} //21
    };
    private static final Response[][] SOFT_RESPONSE = {
        //2  3  4  5  6  7  8  9 10  A
        {H, H, H, DH, DH, H, H, H, H, H}, //13
        {H, H, H, DH, DH, H, H, H, H, H}, //14
        {H, H, DH, DH, DH, H, H, H, H, H}, //15
        {H, H, DH, DH, DH, H, H, H, H, H}, //16
        {H, DH, DH, DH, DH, H, H, H, H, H}, //17
        {S, DS, DS, DS, DS, S, S, H, H, H}, //18
        {S, S, S, S, S, S, S, S, S, S}, //19
        {S, S, S, S, S, S, S, S, S, S}, //20
        {S, S, S, S, S, S, S, S, S, S} //21
    };
    private static final Response[][] PAIR_RESPONSE = {
        //2  3  4  5  6  7  8  9 10  A
        {P, P, P, P, P, P, H, H, H, H}, //2,2
        {P, P, P, P, P, P, H, H, H, H}, //3,3
        {H, H, H, P, P, H, H, H, H, H}, //4,4
        {DH, DH, DH, DH, DH, DH, DH, DH, H, H}, //5,5 - never split 5s (or tens)
        {P, P, P, P, P, H, H, H, H, H}, //6,6
        {P, P, P, P, P, P, H, H, H, H}, //7,7
        {P, P, P, P, P, P, P, P, P, P}, //8,8
        {P, P, P, P, P, S, P, P, S, S}, //9,9
        {S, S, S, S, S, S, S, S, S, S}, //T,T
        {P, P, P, P, P, P, P, P, P, P} //A,A
    };

    public ZeroMemoryPlayerWithMissChance(double chanceWrongResponse) {
        super();
        numberOfMistakes = 0;
        this.chanceWrongResponse = chanceWrongResponse;
    }

    @Override
    public Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit) {
        if (playerHand.isPair()) {
            if (canSplit) {
                //Response correctResponse = getCorrectResponse(PAIR_RESPONSE, playerHand.getShowCard().getTableOrdinal(), dealerHand.getShowCard().getTableOrdinal());

                return getResponse(PAIR_RESPONSE, playerHand.getShowCard().getTableOrdinal(), dealerHand.getShowCard().getTableOrdinal());
                //return PAIR_RESPONSE[playerHand.getShowCard().getTableOrdinal()][dealerHand.getShowCard().getTableOrdinal()];
            } else {
                //if we can't split because of a limit on resplitting,
                //treat the hand as a hard total
                try {
                    //Response correctResponse = getCorrectResponse(HARD_RESPONSE, playerHand.getValue() - 4, dealerHand.getShowCard().getTableOrdinal());
                    return getResponse(HARD_RESPONSE, playerHand.getValue() - 4, dealerHand.getShowCard().getTableOrdinal());
                    //return HARD_RESPONSE[playerHand.getValue() - 4][dealerHand.getShowCard().getTableOrdinal()];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new RuntimeException(playerHand.toString() + " - " + playerHand.getValue());
                }
            }
        } else if (playerHand.isSoft()) {
            //Response correctResponse = getCorrectResponse(SOFT_RESPONSE, playerHand.getValue() - 13, dealerHand.getShowCard().getTableOrdinal());
            return getResponse(SOFT_RESPONSE, playerHand.getValue() - 13, dealerHand.getShowCard().getTableOrdinal());
            //return SOFT_RESPONSE[playerHand.getValue() - 13][dealerHand.getShowCard().getTableOrdinal()];
        } else {
            if (playerHand.getValue() - 4 == 30) {
                System.out.println(playerHand);
            }
            //Response correctResponse = getCorrectResponse(HARD_RESPONSE, playerHand.getValue() - 4, dealerHand.getShowCard().getTableOrdinal());
            return getResponse(HARD_RESPONSE, playerHand.getValue() - 4, dealerHand.getShowCard().getTableOrdinal());
            //return HARD_RESPONSE[playerHand.getValue() - 4][dealerHand.getShowCard().getTableOrdinal()];
        }
    }

    @Override
    public void setNumberOfMistakes(int number) {
        numberOfMistakes = number;
    }

    @Override
    public int getNumberOfMistakes() {
        return numberOfMistakes;
    }

    private Response getResponse(Response[][] table, int playerShowCard, int dealerShowCard) {
        Response correctResponse = getCorrectResponse(table, playerShowCard, dealerShowCard);
        List<Response> possibleWrongResponses = getPossibleWrongResponses(table, playerShowCard, dealerShowCard);
        double genRand = random.nextDouble();
        if (possibleWrongResponses.size() > 0 && chanceWrongResponse >= genRand) {
            numberOfMistakes++;
            int indexRandomPossibleWrongResponse = random.nextInt(possibleWrongResponses.size());
            return possibleWrongResponses.get(indexRandomPossibleWrongResponse);
        }
        return correctResponse;

    }

    private Response getCorrectResponse(Response[][] table, int playerShowCard, int dealerShowCard) {
        return table[playerShowCard][dealerShowCard];
    }

    private List<Response> getPossibleWrongResponses(Response[][] table, int playerShowCard, int dealerShowCard) {
        Response correctResponse = getCorrectResponse(table, playerShowCard, dealerShowCard);
        List<Response> possibleWrongResponses = new ArrayList<>();

        Delta deltas[] = {new Delta(0, -1), new Delta(0, 1), new Delta(-1, 0), new Delta(1, 0)};

        int maxRowIndex = table.length;
        int maxColIndex = table[1].length;

        for (Delta delta : deltas) {
            Response possibleWrongResponse;
            if (!((playerShowCard + delta.getX()) < 0)
                    && !((playerShowCard + delta.getX()) >= maxRowIndex)
                    && !((dealerShowCard + delta.getY()) < 0)
                    && !((dealerShowCard + delta.getY()) >= maxColIndex)) {
                possibleWrongResponse = getCorrectResponse(table, playerShowCard + delta.getX(), dealerShowCard + delta.getY());
                if (correctResponse != possibleWrongResponse) {
                    possibleWrongResponses.add(possibleWrongResponse);
                }
            }
        }

        return possibleWrongResponses;
    }

}
