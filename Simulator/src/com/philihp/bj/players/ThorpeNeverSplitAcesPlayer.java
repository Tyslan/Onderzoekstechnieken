/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philihp.bj.players;

import com.philihp.bj.Response;
import static com.philihp.bj.Response.DH;
import static com.philihp.bj.Response.H;
import static com.philihp.bj.Response.P;
import static com.philihp.bj.Response.S;

/**
 *
 * @author Lorenz
 */
public class ThorpeNeverSplitAcesPlayer extends ThorpeBasicPlayerWithMisChance{
    private static final Response[][] PAIR_RESPONSE = {
        //2  3  4  5  6  7  8  9 10  A
        {P, P, P, P, P, P, H, H, H, H}, //2,2
        {P, P, P, P, P, P, H, H, H, H}, //3,3
        {H, H, H, P, DH, H, H, H, H, H}, //4,4
        {DH, DH, DH, DH, DH, DH, DH, DH, H, H}, //5,5 - never split 5s (or tens)
        {P, P, P, P, P, P, H, H, H, H}, //6,6
        {P, P, P, P, P, P, P, H, S, H}, //7,7
        {P, P, P, P, P, P, P, P, P, P}, //8,8
        {P, P, P, P, P, P, S, P, P, S}, //9,9
        {S, S, S, S, S, S, S, S, S, S}, //T,T
        {S, S, S, S, S, S, S, S, S, S} //A,A
    };
    
    
    public ThorpeNeverSplitAcesPlayer(double chanceWrongResponse) {
        super(chanceWrongResponse);
    }
    
}
