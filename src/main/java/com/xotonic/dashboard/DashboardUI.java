package com.xotonic.dashboard;

import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.*;
import com.vaadin.server.*;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import java.util.ArrayList;

/**
 * Отрисовка UI и управление событиями
 */
@Theme("mytheme")
@Widgetset("com.xotonic.dashboard.DashboardAppWidgetset")
@Push // Аддон для управлением UI из другого потока
public class DashboardUI extends UI {

    
    WeatherUpdater weatherUpdater;
    CurrencyUpdater currencyUpdater;
    VisitorsUpdater visitorsUpdater;
   
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {

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
        vlayout.setExpandRatio(caption, 0.2f);

        vlayout.setComponentAlignment(caption, Alignment.MIDDLE_CENTER);
        vlayout.addComponent(hlayout);
        vlayout.setExpandRatio(hlayout, 0.7f);

        /*
         WEATHER
         */
        final Panel weatherPanel = new Panel("<center>Погода</center>");
        weatherPanel.addStyleName("frame-bg-weather");
        weatherPanel.setSizeFull();

        final ArrayList<String> places = new ArrayList<>();
        places.add("Москва");
        places.add("Санкт-Петербург");
        places.add("Новосибирск");

        final ComboBox placeSelect = new ComboBox("Местоположение", places);

        placeSelect.setWidth(100.0f, Unit.PERCENTAGE);

        placeSelect.setFilteringMode(FilteringMode.CONTAINS);
        placeSelect.setImmediate(true);

        // Отключаем пустой выбор
        placeSelect.setNullSelectionAllowed(false);
        final Label currentTemperature = new Label("-");
        currentTemperature.setStyleName("celcium", true);
        currentTemperature.setCaption("Температура текущая");

        final Label tomorrowTemperature = new Label("-");
        tomorrowTemperature.setCaption("Температура на завтра");
        tomorrowTemperature.setStyleName("celcium", true);

        FormLayout weatherFormLayout = new FormLayout(placeSelect, currentTemperature, tomorrowTemperature);
        Button updateWeatherButton = new Button("\u27F3 Обновить");

        VerticalLayout weatherMainLayout = new VerticalLayout(weatherFormLayout);
        weatherMainLayout.setSizeFull();
        weatherMainLayout.setMargin(true);
        weatherMainLayout.addComponent(updateWeatherButton);
        weatherMainLayout.setComponentAlignment(updateWeatherButton, Alignment.BOTTOM_CENTER);
        weatherPanel.setContent(weatherMainLayout);
        /*
         CURRENCY
         */

        Panel currencyPanel = new Panel("<center>Валюта</center>");
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

        currencyGridLayout.addComponent(usdLabel, 1, 1);
        currencyGridLayout.addComponent(eurLabel, 1, 2);
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


        /*
         VISITORS
         */
        Panel visitorsPanel = new Panel("<center>Счетчик посещений</center>");
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
        HorizontalLayout visitorsMainLayout = new HorizontalLayout(ipUniqueLabel, ipTotalLabel);
        visitorsMainLayout.setSizeFull();
        visitorsMainLayout.setComponentAlignment(ipUniqueLabel, Alignment.MIDDLE_CENTER);
        visitorsMainLayout.setComponentAlignment(ipTotalLabel, Alignment.MIDDLE_CENTER);
        visitorsPanel.setContent(visitorsMainLayout);

        hlayout.setSpacing(true);
        hlayout.addComponent(weatherPanel);
        hlayout.addComponent(currencyPanel);
        hlayout.addComponent(visitorsPanel);

        /*
         FOOTER
         */
        
        /*
          Дата состояния обновляется после запуска любого из листенеров
         (Visitors, Currency, Weather)
        */
        
        Label timeStatusValueLabel = new Label("---");
        timeStatusValueLabel.setCaption("Информация по состоянию на");

        // Получаем IP и выводим
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
        vlayout.setExpandRatio(infoHLayout, 0.1f);
        
        /*
            INITIALIZING UPDATERS
        */
        
        weatherUpdater = new WeatherUpdater(placeSelect, places, currentTemperature, tomorrowTemperature, timeStatusValueLabel);
        currencyUpdater = new CurrencyUpdater(usdLabel, usdDeltaLabel, eurLabel, eurDeltaLabel, timeStatusValueLabel);
        visitorsUpdater = new VisitorsUpdater(ipUniqueLabel, ipTotalLabel);
        
        updateWeatherButton.addClickListener(weatherUpdater);
        updateCurrencyButton.addClickListener(currencyUpdater);

        // Обновление данных происходит в отдельных потоках, чтобы у клиента не
        // задерживалась отрисовка интерфейса
        // Для обновления UI из отдельного потока понадобился thirdparty addon
        // vaadin-push
        this.access(weatherUpdater);
        this.access(currencyUpdater);
        this.access(visitorsUpdater);
    }

    @WebServlet(urlPatterns = "/*", name = "DashboardUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DashboardUI.class, productionMode = false)
    public static class DashboardUIServlet extends VaadinServlet {
    }
    
}
