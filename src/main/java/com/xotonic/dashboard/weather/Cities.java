
package com.xotonic.dashboard.weather;

/**
 * Список городов
 * @author xotonic
 */

public  enum Cities {
    /**
     * Москва
     */
    MSK,
    /**
     * Санкт-Петебург
     */
    SPB,
    /**
     * Новосибирск
     */
    NSK;

    /**
     * Вернуть город по порядковому номеру в списке
     * @param id номер в списке
     * @return
     */
    public static Cities byID(int id) {
        return Cities.values()[id];
    }
}
