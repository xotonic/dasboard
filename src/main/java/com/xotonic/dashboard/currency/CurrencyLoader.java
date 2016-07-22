
package com.xotonic.dashboard.currency;

import com.xotonic.dashboard.ExceptionForUser;

/**
 *
 * @author xotonic
 */
public interface CurrencyLoader {
    public CurrencyData getData() throws ExceptionForUser ;
}
