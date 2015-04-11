package com.philihp.bj;

public interface Player {

    public int bet();

    public Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit);

    public void notify(Card card);

    public void resetCount(int decks);
    
    public void setNumberOfMistakes(int number);
    
    public int getNumberOfMistakes();
}
