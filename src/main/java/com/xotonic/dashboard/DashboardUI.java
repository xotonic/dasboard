package com.xotonic.dashboard;

import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.*;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.*;
import com.xotonic.dashboard.currency.*;
import com.xotonic.dashboard.weather.*;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Отрисовка UI и управление событиями
 */
@Theme("mytheme")
@Widgetset("com.xotonic.dashboard.DashboardAppWidgetset")
@Push // Аддон для управлением UI из другого потока
public class DashboardUI extends UI {

    public WeatherData weatherData = new WeatherData();
    public CurrencyData currencyData = new CurrencyData();
    
    public final int defaultCityId = Cities.NSK.ordinal() - 1;
    public void updateWeather(int id)
    {
        WeatherLoader loader = new ForecastIOLoader();
        weatherData = loader.getData(Cities.values()[id]);
    }
    
    public void updateCurrency()
    {
        CurrencyLoader loader = new CBRLoader();
        currencyData = loader.getData();
    }
    
    private Label timeStatusValueLabel;
    public void updateDateLabel()
    {
        if (timeStatusValueLabel != null)
        timeStatusValueLabel.setValue(Calendar.getInstance().getTime().toString());
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
        UpdateWeatherLisnener updateWeatherListener =  new UpdateWeatherLisnener(placeSelect, places, currentTemperature, tomorrowTemperature);
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
        currencyGridLayout.addComponent(new Label("Курс"), 1, 0);
        currencyGridLayout.addComponent(new Label("Изменение"), 2, 0);
        
        final Label usdLabel = new Label("0");
        final Label usdDeltaLabel = new Label("0");
        final Label eurLabel = new Label("0");
        final Label eurDeltaLabel = new Label("0");
        
        currencyGridLayout.addComponent(usdLabel,  1, 1);
        currencyGridLayout.addComponent(eurLabel,  1, 2);
        currencyGridLayout.addComponent(usdDeltaLabel, 2, 1);
        currencyGridLayout.addComponent(eurDeltaLabel, 2, 2);

        
        VerticalLayout mainCurrencyLayout = new VerticalLayout(currencyGridLayout);
        mainCurrencyLayout.setSizeFull();
        mainCurrencyLayout.setMargin(true);
        mainCurrencyLayout.setSpacing(true);
        Button updateCurrencyButton = new Button("Обновить");
        mainCurrencyLayout.addComponent(updateCurrencyButton);
        mainCurrencyLayout.setComponentAlignment(updateCurrencyButton, Alignment.BOTTOM_CENTER);
        currencyPanel.setContent(mainCurrencyLayout);
        
        UpdateCurrencyListener updateCurrencyListener = new UpdateCurrencyListener(usdLabel, usdDeltaLabel, eurLabel, eurDeltaLabel);
        updateCurrencyButton.addClickListener(updateCurrencyListener);
        
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
        timeStatusValueLabel = new Label("Начало войны");
        timeStatusValueLabel.setCaption("Информация по состоянию на");
        
        
        // Find the context we are running in and get the browser
        // information from that.
        final WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        
        Label ipValueLabel = new Label(webBrowser.getAddress());
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
        this.access(updateCurrencyListener);
    }

    @WebServlet(urlPatterns = "/*", name = "DashboardUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DashboardUI.class, productionMode = false)
    public static class DashboardUIServlet extends VaadinServlet {
    }

    private class UpdateWeatherLisnener implements ClickListener ,Runnable {

        private final ComboBox placeSelect;
        private final ArrayList<String> places;
        private final Label currentTemperature;
        private final Label tomorrowTemperature;

        public UpdateWeatherLisnener(ComboBox placeSelect, ArrayList<String> places, Label currentTemperature, Label tomorrowTemperature) {
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
            updateDateLabel();

        }

        @Override
        public void run() {
            buttonClick(null);
        }
    }
    

    private class UpdateCurrencyListener implements ClickListener,Runnable {

        private final Label usdLabel;
        private final Label usdDeltaLabel;
        private final Label eurLabel;
        private final Label eurDeltaLabel;

        public UpdateCurrencyListener(Label usdLabel, Label usdDeltaLabel, Label eurLabel, Label eurDeltaLabel) {
            this.usdLabel = usdLabel;
            this.usdDeltaLabel = usdDeltaLabel;
            this.eurLabel = eurLabel;
            this.eurDeltaLabel = eurDeltaLabel;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            updateCurrency();
            usdLabel.setValue(Float.toString( currencyData.USD));
            usdDeltaLabel.setValue(Float.toString(currencyData.USDDelta));
            usdDeltaLabel.setStyleName( currencyData.USDDelta < 0 ?
                    "currency-delta-negative" : "currency-delta-positive", true);
            
            eurLabel.setValue(Float.toString(currencyData.EUR));
            eurDeltaLabel.setValue(Float.toString(currencyData.EURDelta));
            eurDeltaLabel.setStyleName( currencyData.EURDelta < 0 ?
                    "currency-delta-negative" : "currency-delta-positive", true);
            Notification.show("Валюта обновлена", Notification.Type.HUMANIZED_MESSAGE );
            updateDateLabel();
        }
        @Override
        public void run() {
            buttonClick(null);
        }
    }
}
