
package com.xotonic.dashboard;


public class OpenWeatherMap implements WeatherLoader {
    /*
        Разработчики рекомендуют использовать в запросах ID городов вместо названий
    */
    private static final int ID_MSK = 524901;
    private static final int ID_SPB = 536203;
    private static final int ID_NSK = 1496747;
    
    private static final String APPKEY = "bd46d8a7882771d67692f6014b0059e7";
    @Override
    public WeatherData getData(Cities city) {
       WeatherData data = new WeatherData();
       data.city = city;
       
       
       
       return data;
    }
    
    
}
