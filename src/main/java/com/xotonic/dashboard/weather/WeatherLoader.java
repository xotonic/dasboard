
package com.xotonic.dashboard.weather;

import com.xotonic.dashboard.ExceptionForUser;

/**
 * Интерфейс для взаимодействия с {@link DashboardUI}
 * @author xotonic
 */
public interface WeatherLoader {
    
    /**
     * @param city Запрашиваемый город
     * @return Информация по этому городу
     * @throws com.xotonic.dashboard.ExceptionForUser
     */
    public WeatherData getData(Cities city) throws ExceptionForUser;
}
