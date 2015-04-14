package com.philihp.bj.players;

import com.philihp.bj.Card;
import com.philihp.bj.Hand;
import com.philihp.bj.Response;

public interface Player {

    public int bet();

    public Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit);

    public void notify(Card card);

    public void resetCount(int decks);
    
    public void setNumberOfMistakes(int number);
    
    public int getNumberOfMistakes();

}
