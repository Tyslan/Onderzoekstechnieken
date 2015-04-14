package com.philihp.bj.players;

import com.philihp.bj.Blackjack;
import com.philihp.bj.Card;
import com.philihp.bj.Hand;
import com.philihp.bj.Response;

public class DealerPlayer implements Player {

    public DealerPlayer() {
    }

    @Override
    public int bet() {
        throw new RuntimeException("Dealer doesn't bet.");
    }

    @Override
    public Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit) {
        if (dealerHand.getValue() < 17) {
            return Response.H;
        } else if (dealerHand.getValue() > 17) {
            return Response.S;
        } else if (dealerHand.isSoft()) {
            return Blackjack.SOFT17;
        } else {
            return Response.S;
        }
    }

    @Override
    public void notify(Card card) {
    }

    @Override
    public void resetCount(int decks) {
    }

    @Override
    public void setNumberOfMistakes(int number) {
        throw new RuntimeException("Dealer doesn't make mistakes");
    }

    @Override
    public int getNumberOfMistakes() {
        throw new RuntimeException("Dealer doesn't make mistakes");
    }
}
