
package com.xotonic.dashboard.weather;

/**
 * Интерфейс для взаимодействия с {@link DashboardUI}
 * @author xotonic
 */
public interface WeatherLoader {
    
    /**
     * @param city Запрашиваемый город
     * @return Информация по этому городу
     */
    public WeatherData getData(Cities city);
}
