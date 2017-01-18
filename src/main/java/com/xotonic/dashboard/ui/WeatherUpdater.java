
package com.xotonic.dashboard.ui;

import com.vaadin.ui.*;
import com.xotonic.dashboard.ExceptionForUser;
import com.xotonic.dashboard.weather.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Обновление прогноза погоды<br>
 * @author xotonic
 */
public class WeatherUpdater implements Button.ClickListener, Runnable {

    /**
     * Информация о погоде в определенном населенном пункте
     */
    private WeatherData weatherData = new WeatherData();
    /**
     * НП, который будет загружаться по умолчанию
     */
    public final int defaultCityId = Cities.NSK.ordinal() - 1;
    /**
     * Выпадающий список с НП
     */
    private final ComboBox placeSelect;
    /**
     * Лист наименований НП
     */
    private final ArrayList<String> places;
    /**
     * Надпись с текущей температурой
     */
    private final Label currentTemperature;
    /**
     * Надпись с температурой на завтра
     */
    private final Label tomorrowTemperature;
    /**
     * Надпись с временем последнего обновления
     */
    private final Label timeStatusValueLabel;
    /**
     * Метка, отключающая вывод сообщения пользователю об успешном обновлении
     */
    private boolean silent;
    /**
     * Формат числа для отображения градусов по Цельсию
     */
    private final DecimalFormat weatherFormat;
    /**
     * @param placeSelect компонент со списком городов ( Порядок городов должен
     * соответсвовать порядку в {@link Cities})
     * @param places массив со списком городов в том же порядке, что и у placeSelect
     * @param currentTemperature текущая температура
     * @param tomorrowTemperature температура на завтра
     * @param timeStatusValueLabel время и дата, которое будет обновляться после
     * обновления погоды
    */
    public WeatherUpdater(ComboBox placeSelect, ArrayList<String> places,
            Label currentTemperature,
            Label tomorrowTemperature,
            Label timeStatusValueLabel) {
        this.placeSelect = placeSelect;
        this.places = places;
        this.currentTemperature = currentTemperature;
        this.tomorrowTemperature = tomorrowTemperature;
        this.timeStatusValueLabel = timeStatusValueLabel;
        this.silent = true;
        weatherFormat = new DecimalFormat("#.#");
        weatherFormat.setRoundingMode(RoundingMode.CEILING);
        placeSelect.select(defaultCityId);
        placeSelect.setValue(places.get(defaultCityId));

    }

    /**
     * Обработчик события по клику на кнопку Обновить
     * @param event
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {
        String selectedCity = (String) placeSelect.getValue();
        int id = places.indexOf(selectedCity);
        updateWeather(id);
        currentTemperature.setValue(weatherFormat.format(weatherData.celcium_today));
        tomorrowTemperature.setValue(weatherFormat.format(weatherData.celcium_tomorrow));
        if (silent == false) {
            Notification.show("Погода обновлена", selectedCity, Notification.Type.HUMANIZED_MESSAGE);
        }
        updateDateLabel();

    }

    /**
     * Обновить текст с временем последнего обновления
     */
    private void updateDateLabel() {
        if (timeStatusValueLabel != null) {
            timeStatusValueLabel.setValue(
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    .format(Calendar.getInstance().getTime()));
        }
    }

    /**
     * Обновить информацию о погоде
     * @param id номер населеного пункта
     */
    private void updateWeather(int id) {
        WeatherDataService loader = new FIODataService();
        try {
            weatherData = loader.getData(Cities.values()[id]);
        } catch (ExceptionForUser e) {
            Notification.show(e.what(), Notification.Type.ERROR_MESSAGE);
        }
    }
    /**
     * Эмуляция нажатия на кнопку обновления при запуске класса как Runnable
     */
    @Override
    public void run() {
        buttonClick(null);
    }

    /**
     *  Вернуть флаг, включающий оповещение пользователя об успешном обновлении
     */
    public boolean isSilent() {
        return silent;
    }

    /**
     * Поставить флаг, включающий оповещение пользователя об успешном обновлении
     */
    public void setSilent(boolean silent) {
        this.silent = silent;
    }
}
