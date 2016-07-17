
package com.xotonic.dashboard.weather;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bitpipeline.lib.owm.*;
import org.json.JSONException;
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
    public WeatherData getData(Cities city)  {
       WeatherData data = new WeatherData();
       data.city = city;
       int  cityId = IDs[city.ordinal()];
       OwmClient owm = new OwmClient();
       owm.setAPPID(APPKEY);
       try {
       StatusWeatherData currentWeather = owm.currentWeatherAtCity(cityId);
       data.celcium_today = currentWeather.getTemp() - 273.0f;
       WeatherForecastResponse response =  owm.forecastWeatherAtCity(cityId);
       if (response.hasForecasts())
       {
           List<ForecastWeatherData> forecasts = response.getForecasts();
           data.celcium_tomorrow = forecasts.get(0).getTemp();

            //for ( ForecastWeatherData d : forecasts)
           //{
           //}
       } else data.celcium_tomorrow = Float.NaN;
        
       } 
        catch (JSONException | IOException ex) {
            Logger.getLogger(OpenWeatherMap.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       return data;
    }
    
    
}
