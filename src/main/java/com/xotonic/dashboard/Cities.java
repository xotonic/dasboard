/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xotonic.dashboard;



public  enum Cities {
    MSK,
    SPB,
    NSK;
    
    public static Cities byID(int id) {
        return Cities.values()[id];
    }
}
