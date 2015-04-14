package com.philihp.bj.players;

import com.philihp.bj.Card;
import com.philihp.bj.Hand;
import com.philihp.bj.Response;
import static com.philihp.bj.Card.*;
import static com.philihp.bj.Response.*;

public class REKOPlayer implements Player {

    private int count = 0;
    ;

	private static final Response[][] HARD_RESPONSE = {
        {H, H, H, H, H, H, H, H, H, H},
        {H, H, H, H, H, H, H, H, H, H},
        {H, H, H, H, H, H, H, H, H, H},
        {H, H, H, H, H, H, H, H, H, H},
        {H, H, H, H, H, H, H, H, H, H},
        {H, DH, DH, DH, DH, H, H, H, H, H},
        {DH, DH, DH, DH, DH, DH, DH, DH, H, H},
        {DH, DH, DH, DH, DH, DH, DH, DH, DH, DH},
        {H, H, S, S, S, H, H, H, H, H},
        {S, S, S, S, S, H, H, H, H, H},
        {S, S, S, S, S, H, H, H, H, H},
        {S, S, S, S, S, H, H, H, H, H},
        {S, S, S, S, S, H, H, H, H, H},
        {S, S, S, S, S, S, S, S, S, S},
        {S, S, S, S, S, S, S, S, S, S},
        {S, S, S, S, S, S, S, S, S, S},
        {S, S, S, S, S, S, S, S, S, S},
        {S, S, S, S, S, S, S, S, S, S}
    };
    private static final Response[][] SOFT_RESPONSE = {
        {H, H, H, DH, DH, H, H, H, H, H},
        {H, H, H, DH, DH, H, H, H, H, H},
        {H, H, DH, DH, DH, H, H, H, H, H},
        {H, H, DH, DH, DH, H, H, H, H, H},
        {H, DH, DH, DH, DH, H, H, H, H, H},
        {DS, DS, DS, DS, DS, S, S, H, H, H},
        {S, S, S, S, DS, S, S, S, S, S},
        {S, S, S, S, S, S, S, S, S, S},
        {S, S, S, S, S, S, S, S, S, S}
    };
    private static final Response[][] PAIR_RESPONSE = {
        {H, H, P, P, P, P, H, H, H, H},
        {H, H, P, P, P, P, H, H, H, H},
        {H, H, H, H, H, H, H, H, H, H},
        {DH, DH, DH, DH, DH, DH, DH, DH, H, H},
        {H, P, P, P, P, H, H, H, H, H},
        {P, P, P, P, P, P, H, H, H, H},
        {P, P, P, P, P, P, P, P, P, P},
        {P, P, P, P, P, S, P, P, S, S},
        {S, S, S, S, S, S, S, S, S, S},
        {P, P, P, P, P, P, P, P, P, P}
    };

    public REKOPlayer() {
    }

    @Override
    public int bet() {
        if (count < 8) {
            return 1;
        } else {
            return 750;
        }
    }

    @Override
    public Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit) {
        if (playerHand.isPair()) {
            if (canSplit) {
                return PAIR_RESPONSE[playerHand.getShowCard().getTableOrdinal()][dealerHand.getShowCard().getTableOrdinal()];
            } else {
				//if we can't split because of a limit on resplitting,
                //treat the hand as a hard total
                try {
                    return HARD_RESPONSE[playerHand.getValue() - 4][dealerHand.getShowCard().getTableOrdinal()];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new RuntimeException(playerHand.toString() + " - " + playerHand.getValue());
                }
            }
        } else if (playerHand.isSoft()) {
            return SOFT_RESPONSE[playerHand.getValue() - 13][dealerHand.getShowCard().getTableOrdinal()];
        } else {
            if (playerHand.getValue() - 4 == 30) {
                System.out.println(playerHand);
            }
            return HARD_RESPONSE[playerHand.getValue() - 4][dealerHand.getShowCard().getTableOrdinal()];
        }
    }

    @Override
    public void notify(Card card) {
        switch (card) {
            case _2:
            case _3:
            case _4:
            case _5:
            case _6:
            case _7:
                count++;
                break;
            case _8:
            case _9:
                break;
            case _A:
            case _T:
                count--;
        }
    }

    @Override
    public void resetCount(int decks) {
        switch (decks) {
            case 1:
                count = -1;
                break;
            case 2:
                count = -5;
                break;
            case 3:
            case 4:
                count = -12;
                break;
            case 5:
            case 6:
                count = -20;
                break;
            default:
                count = -27;
                break;
        }
    }
    
    @Override
    public void setNumberOfMistakes(int number) {
        throw new RuntimeException("This player doesn't make mistakes");
    }

    @Override
    public int getNumberOfMistakes() {
        return 0;
    }
}
