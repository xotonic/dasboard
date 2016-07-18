package com.xotonic.dashboard;

import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.*;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.*;
import com.xotonic.dashboard.weather.*;
import java.util.ArrayList;

/**
 * Отрисовка UI и управление событиями
 */
@Theme("mytheme")
@Widgetset("com.xotonic.dashboard.DashboardAppWidgetset")
@Push // Аддон для управлением UI из другого потока
public class DashboardUI extends UI {

    public WeatherData weatherData = new WeatherData();
    public final int defaultCityId = Cities.NSK.ordinal() - 1;
    public void updateWeather(int id)
    {
        WeatherLoader owm = new ForecastIOLoader();
        weatherData = owm.getData(Cities.values()[id]);
    }
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        // Default city is Moscow
        //updateWeather(defaultCityId);
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.addStyleName("outlined");
        vlayout.setSizeFull();
        vlayout.setMargin(true);
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.addStyleName("outlined");
        hlayout.setSizeFull();
        setContent(vlayout);
        
        
        Label caption = new Label("Тестовое сетевое приложение");
        caption.setStyleName("logo-label", true);
        caption.setWidth(null);
        vlayout.addComponent(caption);
        vlayout.setExpandRatio(caption,0.2f);

        vlayout.setComponentAlignment(caption, Alignment.MIDDLE_CENTER);
        vlayout.addComponent(hlayout);
        vlayout.setExpandRatio(hlayout,0.7f);
        
        
        
        //final ProgressBar pbar = new ProgressBar();
        //pbar.setIndeterminate(true);
        
        /*
         WEATHER
        */
        final Panel weatherPanel =  new Panel("Погода");
        weatherPanel.setSizeFull();
        
        final ArrayList<String> places = new ArrayList<>(); 
        places.add("Москва");
        places.add("Санкт-Петербург");
        places.add("Новосибирск");

        final ComboBox placeSelect = new ComboBox("Местоположение", places);
 
        //placeSelect.setInputPrompt("Место не выбрано");
        placeSelect.select(defaultCityId);
        placeSelect.setWidth(100.0f, Unit.PERCENTAGE);
 
        // Set the appropriate filtering mode for this example
        placeSelect.setFilteringMode(FilteringMode.CONTAINS);
        placeSelect.setImmediate(true);
 
        // Disallow null selections
        placeSelect.setNullSelectionAllowed(false);
        placeSelect.setValue(places.get(defaultCityId));
        final Label currentTemperature = new Label( "-" );
        currentTemperature.setStyleName("celcium", true);
        currentTemperature.setCaption("Температура текущая");
        
        final Label tomorrowTemperature = new Label("-");
        tomorrowTemperature.setCaption("Температура на завтра");
        tomorrowTemperature.setStyleName("celcium", true);

        FormLayout weatherFormLayout = new FormLayout(placeSelect, currentTemperature, tomorrowTemperature);
        Button updateWeatherButton = new Button("Обновить");
        ClickListenerImpl updateWeatherListener =  new ClickListenerImpl(placeSelect, places, currentTemperature, tomorrowTemperature);
        updateWeatherButton.addClickListener(updateWeatherListener);
        
        VerticalLayout weatherMainLayout = new VerticalLayout(weatherFormLayout);
        weatherMainLayout.setSizeFull();
        weatherMainLayout.setMargin(true);
        //weatherMainLayout.addComponent(pbar);
        //weatherMainLayout.setComponentAlignment(pbar, Alignment.BOTTOM_CENTER);
        //pbar.setVisible(false);
        weatherMainLayout.addComponent(updateWeatherButton);
        weatherMainLayout.setComponentAlignment(updateWeatherButton, Alignment.BOTTOM_CENTER);
        weatherPanel.setContent(weatherMainLayout);
        /*
         CURRENCY
        */
        
        Panel currencyPanel =  new Panel("Валюта");
        currencyPanel.setSizeFull();
        GridLayout currencyGridLayout = new GridLayout();
        currencyGridLayout.setSizeFull();
        currencyGridLayout.setRows(3);
        currencyGridLayout.setColumns(3);
        currencyGridLayout.addComponent(new Label("USD"), 0, 1);
        currencyGridLayout.addComponent(new Label("EUR"), 0, 2);
        currencyGridLayout.addComponent(new Label("Вчера"), 1, 0);
        currencyGridLayout.addComponent(new Label("Сегодня"), 2, 0);
        
        Label usdTodayLabel = new Label("0");
        Label usdYstrdyLabel = new Label("0");
        Label eurTodayLabel = new Label("0");
        Label eurYstrdyLabel = new Label("0");

        currencyGridLayout.addComponent(usdTodayLabel,  1, 2);
        currencyGridLayout.addComponent(usdYstrdyLabel, 1, 1);
        currencyGridLayout.addComponent(eurTodayLabel,  2, 1);
        currencyGridLayout.addComponent(eurYstrdyLabel, 2, 2);

        
        VerticalLayout mainCurrencyLayout = new VerticalLayout(currencyGridLayout);
        mainCurrencyLayout.setSizeFull();
        mainCurrencyLayout.setMargin(true);
        mainCurrencyLayout.setSpacing(true);
        Button updateCurrencyButton = new Button("Обновить");
        mainCurrencyLayout.addComponent(updateCurrencyButton);
        mainCurrencyLayout.setComponentAlignment(updateCurrencyButton, Alignment.BOTTOM_CENTER);
        currencyPanel.setContent(mainCurrencyLayout);
        /*
         VISITORS
        */
        Panel visitorsPanel =  new Panel("Счетчик посещений");
        Label ipCountLabel = new Label("1488");
        ipCountLabel.setSizeUndefined();
        ipCountLabel.setStyleName("visitors-counter-label", true);
        visitorsPanel.setSizeFull();
        VerticalLayout visitorsMainLayout = new VerticalLayout(ipCountLabel);
        visitorsMainLayout.setSizeFull();
        visitorsMainLayout.setComponentAlignment(ipCountLabel, Alignment.MIDDLE_CENTER);
        visitorsPanel.setContent( visitorsMainLayout);
       
        hlayout.setSpacing(true);
        hlayout.addComponent(weatherPanel);
        hlayout.addComponent(currencyPanel);
        hlayout.addComponent(visitorsPanel);
         /*
         FOOTER
        */
        Label timeStatusValueLabel = new Label("Начало войны");
        timeStatusValueLabel.setCaption("Информация по состоянию на");
        Label ipValueLabel = new Label("192.168.1.1");
        ipValueLabel.setCaption("Ваш IP");

        HorizontalLayout infoHLayout = new HorizontalLayout();
        infoHLayout.addStyleName("outlined");
        infoHLayout.setSizeFull();

        infoHLayout.addComponent(timeStatusValueLabel);
        timeStatusValueLabel.setWidth(null);
        infoHLayout.setComponentAlignment(timeStatusValueLabel, Alignment.BOTTOM_LEFT);

        infoHLayout.addComponent(ipValueLabel);
        ipValueLabel.setWidth(null);
        infoHLayout.setComponentAlignment(ipValueLabel, Alignment.BOTTOM_RIGHT);


        vlayout.addComponent(infoHLayout);
        vlayout.setExpandRatio(infoHLayout,0.1f);
        
        this.access(updateWeatherListener); 

    }

    @WebServlet(urlPatterns = "/*", name = "DashboardUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DashboardUI.class, productionMode = false)
    public static class DashboardUIServlet extends VaadinServlet {
    }

    private class ClickListenerImpl implements ClickListener ,Runnable {

        private final ComboBox placeSelect;
        private final ArrayList<String> places;
        private final Label currentTemperature;
        private final Label tomorrowTemperature;

        public ClickListenerImpl(ComboBox placeSelect, ArrayList<String> places, Label currentTemperature, Label tomorrowTemperature) {
            this.placeSelect = placeSelect;
            this.places = places;
            this.currentTemperature = currentTemperature;
            this.tomorrowTemperature = tomorrowTemperature;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            //pbar.setVisible(true);
            String selectedCity = (String)placeSelect.getValue();
            int id = places.indexOf(selectedCity);
            updateWeather(id);
            currentTemperature.setValue(Float.toString(weatherData.celcium_today));
            tomorrowTemperature.setValue(Float.toString(weatherData.celcium_tomorrow));
            //pbar.setVisible(false);
            Notification.show("Погода обновлена",selectedCity, Notification.Type.HUMANIZED_MESSAGE );
        }

        @Override
        public void run() {
            buttonClick(null);
        }
    }
}
