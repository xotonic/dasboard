package com.xotonic.dashboard;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

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
        setContent(vlayout);
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.addStyleName("outlined");
        hlayout.setSizeFull();
        
        
        Label caption = new Label("Тестовое сетевое приложение", ContentMode.HTML);
        vlayout.addComponent(caption);

        vlayout.addComponent(hlayout);
        
        Panel weatherPanel =  new Panel("Погода");
        weatherPanel.setSizeFull();
        Panel currencyPanel =  new Panel("Валюта");
        currencyPanel.setSizeFull();
        Panel visitorsPanel =  new Panel("Счетчик посещений");
        visitorsPanel.setSizeFull();
        hlayout.addComponent(weatherPanel);
        hlayout.addComponent(currencyPanel);
        hlayout.addComponent(visitorsPanel);

    }

    @WebServlet(urlPatterns = "/*", name = "DashboardUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DashboardUI.class, productionMode = false)
    public static class DashboardUIServlet extends VaadinServlet {
    }
}
