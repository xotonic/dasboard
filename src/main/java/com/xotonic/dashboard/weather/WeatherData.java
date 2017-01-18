
package com.xotonic.dashboard.weather;

/**
 * Класс для передачи информации от загрузчиков погодных информеров
 * ({@link WeatherDataService})
 * @author xotonic
 */
public class WeatherData {
    /**
     * Значение температуры за сегодня в градусах по Цельсию
     */
    public float celcium_today;
    /**
     * Значение температуры на завтра в градусах по Цельсию
     */
    public float celcium_tomorrow;
    /**
     * Город, для которого предназначен прогноз
     */
    public Cities city;
}
