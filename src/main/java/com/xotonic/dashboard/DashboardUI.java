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
import com.xotonic.dashboard.visitors.*;
import com.xotonic.dashboard.weather.*;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Отрисовка UI и управление событиями
 */
@Theme("mytheme")
@Widgetset("com.xotonic.dashboard.DashboardAppWidgetset")
@Push // Аддон для управлением UI из другого потока
public class DashboardUI extends UI {

    
    /*
        Основная информация, реализация скрыта
        Просто получаем ее через интерфейсы
    */
    // Погода
    public WeatherData weatherData = new WeatherData();
    // Курсы валют
    public CurrencyData currencyData = new CurrencyData();
    // Посещения
    public VisitorsData visitorsData = new VisitorsData();
    
    // Получение этой информации
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
    
    public void updateVisitors()
    {
        VisitorsLoader loader = new MongoVisitorsLoader();
        visitorsData = loader.getData();
    }
    public void upsertAddress(String ip)
    {
        new MongoVisitorsLoader().registerIP(ip);
    }
    private Label timeStatusValueLabel;
    public void updateDateLabel()
    {
        if (timeStatusValueLabel != null)
        timeStatusValueLabel.setValue(
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        .format( Calendar.getInstance().getTime()));
    }
    
    // Форматирование для разлиных величин
    
    DecimalFormat currencyFormat;
    DecimalFormat currencyDeltaFormat;

    DecimalFormat weatherFormat;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        currencyFormat = new DecimalFormat("#.####");
        currencyFormat.setRoundingMode(RoundingMode.CEILING);
        currencyDeltaFormat = new DecimalFormat("+#.###;-#.###");
        currencyDeltaFormat.setRoundingMode(RoundingMode.CEILING);
        weatherFormat = new DecimalFormat("#.#");
        weatherFormat.setRoundingMode(RoundingMode.CEILING);
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.addStyleName("outlined");
        vlayout.addStyleName("bg");
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
        final Panel weatherPanel =  new Panel("<center>Погода</center>");
        weatherPanel.addStyleName("frame-bg-weather");
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
        Button updateWeatherButton = new Button("\u27F3 Обновить");
        UpdateWeatherLisnener updateWeatherListener =  new UpdateWeatherLisnener(placeSelect, places, currentTemperature, tomorrowTemperature);
        updateWeatherButton.addClickListener(updateWeatherListener);
        
        VerticalLayout weatherMainLayout = new VerticalLayout(weatherFormLayout);
        weatherMainLayout.setSizeFull();
        weatherMainLayout.setMargin(true);
        weatherMainLayout.addComponent(updateWeatherButton);
        weatherMainLayout.setComponentAlignment(updateWeatherButton, Alignment.BOTTOM_CENTER);
        weatherPanel.setContent(weatherMainLayout);
        /*
         CURRENCY
        */
        
        Panel currencyPanel =  new Panel("<center>Валюта</center>");
        currencyPanel.addStyleName("frame-bg-currency");

        currencyPanel.setSizeFull();
        GridLayout currencyGridLayout = new GridLayout();
        currencyGridLayout.setSizeFull();
        currencyGridLayout.setRows(3);
        currencyGridLayout.setColumns(3);
        Label usd = new Label("$ USD");
        Label eur = new Label("\u20AC EUR");
        usd.setStyleName("currency", true);
        eur.setStyleName("currency", true);

        currencyGridLayout.addComponent(usd, 0, 1);
        currencyGridLayout.addComponent(eur, 0, 2);
        currencyGridLayout.addComponent(new Label("Курс"), 1, 0);
        currencyGridLayout.addComponent(new Label("Изменение"), 2, 0);
        
        final Label usdLabel = new Label("0");
        final Label usdDeltaLabel = new Label("0");
        final Label eurLabel = new Label("0");
        final Label eurDeltaLabel = new Label("0");
        usdLabel.setStyleName("currency", true);
        usdDeltaLabel.setStyleName("currency", true);
        eurLabel.setStyleName("currency", true);
        eurDeltaLabel.setStyleName("currency", true);
        
        
        currencyGridLayout.addComponent(usdLabel,  1, 1);
        currencyGridLayout.addComponent(eurLabel,  1, 2);
        currencyGridLayout.addComponent(usdDeltaLabel, 2, 1);
        currencyGridLayout.addComponent(eurDeltaLabel, 2, 2);

        
        VerticalLayout mainCurrencyLayout = new VerticalLayout(currencyGridLayout);
        mainCurrencyLayout.setSizeFull();
        mainCurrencyLayout.setMargin(true);
        mainCurrencyLayout.setSpacing(true);
        Button updateCurrencyButton = new Button("\u27F3 Обновить");
        mainCurrencyLayout.addComponent(updateCurrencyButton);
        mainCurrencyLayout.setComponentAlignment(updateCurrencyButton, Alignment.BOTTOM_CENTER);
        currencyPanel.setContent(mainCurrencyLayout);
        
        UpdateCurrencyListener updateCurrencyListener = new UpdateCurrencyListener(usdLabel, usdDeltaLabel, eurLabel, eurDeltaLabel);
        updateCurrencyButton.addClickListener(updateCurrencyListener);
        
        /*
         VISITORS
        */
        Panel visitorsPanel =  new Panel("<center>Счетчик посещений</center>");
        visitorsPanel.addStyleName("frame-bg-visitors");
        Label ipUniqueLabel = new Label("0");
        ipUniqueLabel.setCaption("Уникальных");
        ipUniqueLabel.setSizeUndefined();
        ipUniqueLabel.setStyleName("visitors-counter-label", true);
        
        Label ipTotalLabel = new Label("0");
        ipTotalLabel.setCaption("Всего");
        ipTotalLabel.setSizeUndefined();
        ipTotalLabel.setStyleName("visitors-counter-label", true);
        
        visitorsPanel.setSizeFull();
        HorizontalLayout visitorsMainLayout = new HorizontalLayout(ipUniqueLabel,ipTotalLabel);
        visitorsMainLayout.setSizeFull();
        visitorsMainLayout.setComponentAlignment(ipUniqueLabel, Alignment.MIDDLE_CENTER);
        visitorsMainLayout.setComponentAlignment(ipTotalLabel, Alignment.MIDDLE_CENTER);
        visitorsPanel.setContent( visitorsMainLayout);
       
        hlayout.setSpacing(true);
        hlayout.addComponent(weatherPanel);
        hlayout.addComponent(currencyPanel);
        hlayout.addComponent(visitorsPanel);
        
        UpdateVisitorsListener updateVisitorsListener = new UpdateVisitorsListener(ipUniqueLabel, ipTotalLabel);
         /*
         FOOTER
        */
        timeStatusValueLabel = new Label("---");
        timeStatusValueLabel.setCaption("Информация по состоянию на");
        
        
        // Find the context we are running in and get the browser
        // information from that.
        final WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        
        upsertAddress(webBrowser.getAddress());
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
        this.access(updateVisitorsListener);
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
            String selectedCity = (String)placeSelect.getValue();
            int id = places.indexOf(selectedCity);
            updateWeather(id);
            currentTemperature.setValue(weatherFormat.format(weatherData.celcium_today));
            tomorrowTemperature.setValue(weatherFormat.format(weatherData.celcium_tomorrow));
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
            usdLabel.setValue(currencyFormat.format(currencyData.USD));
            usdDeltaLabel.setValue(currencyDeltaFormat.format(currencyData.USDDelta));
            usdDeltaLabel.setStyleName( currencyData.USDDelta < 0 ?
                    "currency-delta-negative" : "currency-delta-positive", true);
            
            eurLabel.setValue(currencyFormat.format(currencyData.EUR));
            eurDeltaLabel.setValue(currencyDeltaFormat.format(currencyData.EURDelta));
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
     private class UpdateVisitorsListener implements ClickListener,Runnable {
        private final Label uniqueLabel;
        private final Label totalLabel;
        public UpdateVisitorsListener(Label uniqueLabel, Label totalLabel) {
            this.uniqueLabel = uniqueLabel;
            this.totalLabel = totalLabel;
        }
        @Override
        public void buttonClick(ClickEvent event) {
            updateVisitors();
            uniqueLabel.setValue(Integer.toString(visitorsData.unique) );
            totalLabel.setValue(Integer.toString(visitorsData.total) );
        }

        @Override
        public void run() {
            buttonClick(null);
        }
 }
}
