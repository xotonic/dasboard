
package com.xotonic.dashboard;

import com.vaadin.ui.*;
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

    // Погода
    private WeatherData weatherData = new WeatherData();
    public final int defaultCityId = Cities.NSK.ordinal() - 1;
    private final ComboBox placeSelect;
    private final ArrayList<String> places;
    private final Label currentTemperature;
    private final Label tomorrowTemperature;
    private final Label timeStatusValueLabel;
    private boolean silent;

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

    private void updateDateLabel() {
        if (timeStatusValueLabel != null) {
            timeStatusValueLabel.setValue(
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    .format(Calendar.getInstance().getTime()));
        }
    }

    private void updateWeather(int id) {
        WeatherLoader loader = new ForecastIOLoader();
        try {
            weatherData = loader.getData(Cities.values()[id]);
        } catch (ExceptionForUser e) {
            Notification.show(e.what(), Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public void run() {
        buttonClick(null);
    }

    /**
     * @return the silent
     */
    public boolean isSilent() {
        return silent;
    }

    /**
     * @param silent the silent to set
     */
    public void setSilent(boolean silent) {
        this.silent = silent;
    }
}
