
package com.xotonic.dashboard.currency;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
/**
 * Парсинг курса валют с сайта Центрального Банка России
 * @author xotonic
 */
public class CBRLoader implements CurrencyLoader {
    private final String ID_USD = "R01235";
    private final String ID_EUR = "R01239";

    @Override
    public CurrencyData getData() {
        CurrencyData cd = new CurrencyData();
        cd.USD = 31.0f;
        cd.EUR = 41.0f;
        cd.EURDelta = 2.0f;
        cd.USDDelta = -1.0f;
        return cd;
    }
    
    

}
