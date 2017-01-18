
package com.xotonic.dashboard.currency;

import com.xotonic.dashboard.ExceptionForUser;

/**
 * Интерфейс для службы получения курса валют
 * @author xotonic
 */
public interface CurrencyDataService {
    public CurrencyData getData() throws ExceptionForUser ;
}
