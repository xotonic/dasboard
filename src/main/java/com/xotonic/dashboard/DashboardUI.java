package com.xotonic.dashboard;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import java.util.ArrayList;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.xotonic.dashboard.DashboardAppWidgetset")
public class DashboardUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
       
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.addStyleName("outlined");
        vlayout.setSizeFull();
        vlayout.setMargin(true);
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.addStyleName("outlined");
        hlayout.setSizeFull();
        setContent(vlayout);
        
        
        Label caption = new Label("Тестовое сетевое приложение", ContentMode.HTML);
        caption.setWidth(null);
        vlayout.addComponent(caption);
        vlayout.setExpandRatio(caption,0.1f);

        vlayout.setComponentAlignment(caption, Alignment.MIDDLE_CENTER);
        vlayout.addComponent(hlayout);
        vlayout.setExpandRatio(hlayout,0.8f);
        
        
        /*
         WEATHER
        */
        Panel weatherPanel =  new Panel("Погода");
        weatherPanel.setSizeFull();
        
        ArrayList<String> places = new ArrayList<>(); 
        places.add("Москва");
        places.add("Новосибирск");
        places.add("Санкт-Петербург");

        ComboBox placeSelect = new ComboBox("Местоположение", places);
 
        placeSelect.setInputPrompt("Место не выбрано");
 
        // Sets the combobox to show a certain property as the item caption
        //sample.setItemCaptionPropertyId(ExampleUtil.iso3166_PROPERTY_NAME);
        //sample.setItemCaptionMode(ItemCaptionMode.PROPERTY);
 
        // Sets the icon to use with the items
        //sample.setItemIconPropertyId(ExampleUtil.iso3166_PROPERTY_FLAG);
 
        // Set full width
        placeSelect.setWidth(100.0f, Unit.PERCENTAGE);
 
        // Set the appropriate filtering mode for this example
        placeSelect.setFilteringMode(FilteringMode.CONTAINS);
        placeSelect.setImmediate(true);
 
        // Disallow null selections
        placeSelect.setNullSelectionAllowed(false);
        
        Label currentTemperature = new Label("12");
        currentTemperature.setCaption("Температура текущая");
        
        Label tomorrowTemperature = new Label("12");
        tomorrowTemperature.setCaption("Температура на завтра");
        
        FormLayout weatherMainLayout = new FormLayout(placeSelect, currentTemperature, tomorrowTemperature);
        weatherPanel.setContent(weatherMainLayout);
        /*
         CURRENCY
        */
        
        Panel currencyPanel =  new Panel("Валюта");
        currencyPanel.setSizeFull();
        Panel visitorsPanel =  new Panel("Счетчик посещений");
        visitorsPanel.setSizeFull();
        hlayout.setSpacing(true);
        hlayout.addComponent(weatherPanel);
        hlayout.addComponent(currencyPanel);
        hlayout.addComponent(visitorsPanel);
        
        //Label timeStatusLabel = new Label("Информация по состоянию на ");
        Label timeStatusValueLabel = new Label("Начало войны");
        //Label ipLabel = new Label("Ваш IP");
        //ipLabel.
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

    }

    @WebServlet(urlPatterns = "/*", name = "DashboardUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DashboardUI.class, productionMode = false)
    public static class DashboardUIServlet extends VaadinServlet {
    }
}
