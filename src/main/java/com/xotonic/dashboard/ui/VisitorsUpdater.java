package com.xotonic.dashboard.ui;

import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.xotonic.dashboard.ExceptionForUser;
import com.xotonic.dashboard.visitors.MongoVisitorsDataService;
import com.xotonic.dashboard.visitors.VisitorsData;
import com.xotonic.dashboard.visitors.VisitorsDataService;

/**
 * Обновление счетчика посещения<br>
 * Берет контроль над 2 UI компонентами:<br>
 * + UniqueLabel - метка для записи числа уникальных IP<br>
 * + TotalLabel - метка для записи общего числа посещений
 * @author xotonic
 */
public class VisitorsUpdater implements Button.ClickListener, Runnable {

    private final Label uniqueLabel;
    private final Label totalLabel;
    private VisitorsData visitorsData = new VisitorsData();

    public VisitorsUpdater(Label uniqueLabel, Label totalLabel) {
        this.uniqueLabel = uniqueLabel;
        this.totalLabel = totalLabel;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        final WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        upsertAddress(webBrowser.getAddress());
        updateVisitors();
        uniqueLabel.setValue(Integer.toString(visitorsData.unique));
        totalLabel.setValue(Integer.toString(visitorsData.total));
    }

    @Override
    public void run() {
        buttonClick(null);
    }

    private void updateVisitors() {
        VisitorsDataService loader = new MongoVisitorsDataService();
        try {
            visitorsData = loader.getData();
        } catch (ExceptionForUser e) {
            Notification.show(e.what(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void upsertAddress(String ip) {
        try {
            new MongoVisitorsDataService().registerIP(ip);
        } catch (ExceptionForUser e) {
            Notification.show(e.what(), Notification.Type.ERROR_MESSAGE);
        }
    }
}
