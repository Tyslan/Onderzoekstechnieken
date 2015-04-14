/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philihp.bj.players;

import com.philihp.bj.Response;
import static com.philihp.bj.Response.DH;
import static com.philihp.bj.Response.DS;
import static com.philihp.bj.Response.H;
import static com.philihp.bj.Response.P;
import static com.philihp.bj.Response.RH;
import static com.philihp.bj.Response.S;

/**
 *
 * @author Lorenz
 */
public class ThorpeBasicPlayerWithMisChance extends ZeroMemoryPlayerWithMissChance{
     private static final Response[][] HARD_RESPONSE = {
        //2  3  4  5  6  7  8  9 10  A
        {H, H, H, H, H, H, H, H, H, H}, //4
        {H, H, H, H, H, H, H, H, H, H}, //5
        {H, H, H, H, H, H, H, H, H, H}, //6
        {H, H, H, H, H, H, H, H, H, H}, //7
        {H, H, H, DH, DH, H, H, H, H, H}, //8
        {DH, DH, DH, DH, DH, H, H, H, H, H}, //9
        {DH, DH, DH, DH, DH, H, H, H, H, H}, //10
        {DH, DH, DH, DH, DH, DH, DH, DH, H, H}, //11 last could be DH?
        {H, H, S, S, S, H, H, H, H, H}, //12
        {S, S, S, S, S, H, H, H, H, H}, //13
        {S, S, S, S, S, H, H, H, H, H}, //14
        {S, S, S, S, S, H, H, H, H, H}, //15
        {S, S, S, S, S, H, H, H, H, H}, //16
        {S, S, S, S, S, S, S, S, S, S}, //17
        {S, S, S, S, S, S, S, S, S, S}, //18
        {S, S, S, S, S, S, S, S, S, S}, //19
        {S, S, S, S, S, S, S, S, S, S}, //20
        {S, S, S, S, S, S, S, S, S, S} //21
    };
    private static final Response[][] SOFT_RESPONSE = {
        //2  3  4  5  6  7  8  9 10  A
        {H, H, DH, DH, DH, H, H, H, H, H}, //13
        {H, H, DH, DH, DH, H, H, H, H, H}, //14
        {H, H, DH, DH, DH, H, H, H, H, H}, //15
        {H, H, DH, DH, DH, H, H, H, H, H}, //16
        {DH, DH, DH, DH, DH, H, H, H, H, H}, //17
        {S, DS, DS, DS, DS, S, S, H, H, S}, //18
        {S, S, S, S, S, S, S, S, S, S}, //19
        {S, S, S, S, S, S, S, S, S, S}, //20
        {S, S, S, S, S, S, S, S, S, S} //21
    };
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
        {P, P, P, P, P, P, P, P, P, P} //A,A
    };
    
    public ThorpeBasicPlayerWithMisChance(double chanceWrongResponse){
        super(chanceWrongResponse);
    }
}
