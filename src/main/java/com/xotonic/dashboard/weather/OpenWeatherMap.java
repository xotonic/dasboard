package com.xotonic.dashboard.weather;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bitpipeline.lib.owm.*;
import org.json.JSONException;

/**
*    Загрузчик погоды с openweathermap.com.<br>
*    <b>Баг:</b> Загружает одинаковый прогноз для всех городов
*    @author xotonic
*    @deprecated используйте {@link ForecastIO}. 
*/
@Deprecated
public class OpenWeatherMap implements WeatherLoader {

    /*
        Разработчики рекомендуют использовать в запросах ID городов вместо названий
     */
    private static final int IDs[] = {
        524901, // MSK
        536203, // SPB
        1496747 // NSK
    };

    private static final String APPKEY = "bd46d8a7882771d67692f6014b0059e7";

    @Override
    public WeatherData getData(Cities city) {
        WeatherData data = new WeatherData();
        data.city = city;
        int cityId = IDs[city.ordinal()];
        OwmClient owm = new OwmClient();
        owm.setAPPID(APPKEY);
        try {
            StatusWeatherData currentWeather = owm.currentWeatherAtCity(cityId);
            data.celcium_today = currentWeather.getTemp() - 273.0f;

            /*
             Получение прогноза
             Интервал между измерениями  - 3 часа
            */
            WeatherForecastResponse response = owm.forecastWeatherAtCity(cityId);
            
            System.out.format("Model: %s\nUnits: %s\nCity: %s\nUrl: %s\n",
                    response.getModel(),
                    response.getUnits(),
                    response.getCity().getName(),
                    response.getUrl());
            if (response.hasForecasts()) {
                List<ForecastWeatherData> forecasts = response.getForecasts();

                /*
                 Минимальная дата, которая может считаться следующий днем
                */
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.add(Calendar.SECOND, -1);
                Date tom = cal.getTime();
                
                /*
                    Поиск первой даты, большей минимальной
                */
                for (ForecastWeatherData d : forecasts) 
                {
                    Date dt = new Date((long) d.getCalcDateTime() * 1000);

                    if (tom.before(dt))
                    {
                        System.out.format("Found date [%s]\n", dt.toString());
                        /*
                         !!! Баг (?)
                         Для разных городов присылается одинаковый прогноз
                        */
                        data.celcium_tomorrow = d.getTemp();
                        break;
                    }
                  

                }
            } else {
                data.celcium_tomorrow = Float.NaN;
            }

        } catch (JSONException | IOException ex) {
            Logger.getLogger(OpenWeatherMap.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

}
