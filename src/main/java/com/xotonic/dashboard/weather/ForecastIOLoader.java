package com.xotonic.dashboard.weather;

import com.github.dvdme.ForecastIOLib.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.*;
/**
 * Загрузчик прогноза погоды с forecast.io.<br>
 * <b>Ахтунг!</b> Сервис предоставляет ограниченноое количество запросов (всего 
 * 1000 единовременно)
 * @author xotonic
 */
public class ForecastIOLoader implements WeatherLoader {
    
    // Бесплатный APIKEY 
    private static final String APPKEY = "7a3f541cf9e9d01fd1be5eff81a80ee8";

    // Широты городов
    private static final String lats[] = 
    {
        "55.7522200", // MSK
        "59.9386300", // SPB
        "55.0415000"  // NSK
    };
    // Долготы
    private static final String lons[] = 
    {
        "37.6155600",// MSK
        "30.3141300",// SPB
        "82.9346000" // NSK
    };
    
    @Override
    public WeatherData getData(Cities city) {
        WeatherData data = new WeatherData();
        data.city = city;
        data.celcium_tomorrow = -1;
        
        String lat = lats[city.ordinal()];
        String lon = lons[city.ordinal()];
        
        ForecastIO fio = new ForecastIO(APPKEY);
        fio.setUnits(ForecastIO.UNITS_SI);
        fio.setLang(ForecastIO.LANG_ENGLISH);
        fio.getForecast(lat, lon);
        System.out.println("Latitude: " + fio.getLatitude());
        System.out.println("Longitude: " + fio.getLongitude());
        System.out.println("Timezone: " + fio.getTimezone());
        //System.out.println("Offset: " + fio.getOffset());

        FIOCurrently currently = new FIOCurrently(fio);
        //Print currently data
        System.out.println("\nCurrently\n");
        /*String[] f = currently.get().getFieldsArray();
        for (int i = 0; i < f.length; i++) {
            System.out.println(f[i] + ": " + currently.get().getByKey(f[i]));
        }*/
        double currentT = currently.get().temperature();
        SimpleDateFormat parserSDF = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date currentTDate = null;

        data.celcium_today = (float)currentT;

        /*
                 Минимальная дата, которая может считаться следующий днем
         */
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date tom = cal.getTime();

        try {
            currentTDate = (Date) parserSDF.parseObject(currently.get().time());
            System.out.format("%s %f\n", currentTDate.toString(), currentT);
        } catch (ParseException ex) {
            Logger.getLogger(ForecastIOLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        FIODaily daily = new FIODaily(fio);
        //In case there is no daily data available
        if (daily.days() < 0) {
            System.out.println("No daily data.");
        } else {
            System.out.println("\nDaily:\n");
        }
        //Print daily data
        for (int i = 0; i < daily.days(); i++) {
            try {
                double t = (daily.getDay(i).apparentTemperatureMax() + daily.getDay(i).apparentTemperatureMin()) / 2;
                Date tDate = (Date) parserSDF.parseObject(daily.getDay(i).time());
                //System.out.format("%s %f\n", tDate.toString(), t);
                if (tDate.after(tom))
                {
                    data.celcium_tomorrow = (float)t;
                    break;
                }
            } catch (ParseException ex) {
                Logger.getLogger(ForecastIOLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return data;
    }

}
