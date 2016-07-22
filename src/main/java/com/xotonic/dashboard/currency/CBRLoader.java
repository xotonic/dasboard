package com.xotonic.dashboard.currency;

import com.xotonic.dashboard.ExceptionForUser;
import java.io.IOException;
import java.io.StringWriter;
import java.math.RoundingMode;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Парсинг курса валют с сайта Центрального Банка России
 *
 * @author xotonic
 */
public class CBRLoader implements CurrencyLoader {

    private final String ID_USD = "R01235";
    private final String ID_EUR = "R01239";

    private final String URL = "http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=%s&date_req2=%s&VAL_NM_RQ=%s";

    private String buildUrl(Date from, Date to, String id) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return String.format(URL, format.format(from), format.format(to), id);
    }

    private void setLastWorkingDay(Calendar cal) {
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_YEAR, -2);
        }
    }

    @Override
    public CurrencyData getData() throws ExceptionForUser {
        CurrencyData cd = new CurrencyData();
        cd.USD = Float.NaN;
        cd.EUR = Float.NaN;
        cd.EURDelta = Float.NaN;
        cd.USDDelta = Float.NaN;
        
        
        /* 
            У ЦБ в понедельник и воскресение выходные.
            Т.е. в эти дни нет записей курса в xml
            Вместо них берем субботу
            Ахтунг! Не учитываются праздники!
         */
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        setLastWorkingDay(cal);
        Date to = cal.getTime();

        cal.add(Calendar.DAY_OF_YEAR, -1);
        setLastWorkingDay(cal);
        Date from = cal.getTime();

        /*
            USD
        */
        
        String urlUSD = buildUrl(from, to, ID_USD);
        System.out.println(urlUSD);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            URL url = new URL(urlUSD);
            Document doc = db.parse(url.openStream());
            /*
            Вывод всего XML
            
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            System.out.println("XML IN String format is: \n" + writer.toString());
             */
            NodeList records = doc.getElementsByTagName("Value");

            String value = records.item(0).getTextContent();
            String lastValue = records.item(1).getTextContent();
            float cur = Float.parseFloat(value.replace(',', '.'));
            float curLast = Float.parseFloat(lastValue.replace(',', '.'));

            cd.USD = cur;
            cd.USDDelta = cur - curLast;

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(CBRLoader.class.getName()).log(Level.SEVERE, null, ex);
            /*    
            
            } catch (TransformerConfigurationException ex) {
            Logger.getLogger(CBRLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(CBRLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
            */
            throw new ExceptionForUser("Ошибка запроса валюты " + ex.getMessage());

        }
        
        /*
            EUR
        */
        
        String urlEUR = buildUrl(from, to, ID_EUR);
        System.out.println(urlEUR);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            URL url = new URL(urlEUR);
            Document doc = db.parse(url.openStream());
            
            NodeList records = doc.getElementsByTagName("Value");

            String value = records.item(0).getTextContent();
            String lastValue = records.item(1).getTextContent();
            float cur = Float.parseFloat(value.replace(',', '.'));
            float curLast = Float.parseFloat(lastValue.replace(',', '.'));

            cd.EUR = cur;
            cd.EURDelta = cur - curLast;

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(CBRLoader.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExceptionForUser("Ошибка запроса валюты "+ex.getMessage());
        }
                
       
        
        return cd;
    }
}
