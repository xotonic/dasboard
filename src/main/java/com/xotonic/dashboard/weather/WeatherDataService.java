
package com.xotonic.dashboard.weather;

import com.xotonic.dashboard.ExceptionForUser;
import com.xotonic.dashboard.ui.DashboardUI;

/**
 * Интерфейс для взаимодействия с {@link DashboardUI}
 * @author xotonic
 */
public interface WeatherDataService {
    
    /**
     * @param city Запрашиваемый город
     * @return Информация по этому городу
     * @throws com.xotonic.dashboard.ExceptionForUser
     */
    WeatherData getData(Cities city) throws ExceptionForUser;
}
