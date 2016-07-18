
package com.xotonic.dashboard.weather;

/**
 * Список городов
 * @author xotonic
 */

public  enum Cities {
    MSK,
    SPB,
    NSK;
    
    public static Cities byID(int id) {
        return Cities.values()[id];
    }
}
