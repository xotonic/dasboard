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

 * @author xotonic
 */
public class VisitorsUpdater implements Button.ClickListener, Runnable {

    /**
     * Метка для записи числа уникальных IP
     */
    private final Label uniqueLabel;
    /**
     * Метка для записи общего числа посещений
     */
    private final Label totalLabel;
    /**
     * Информация о посещениях
     */
    private VisitorsData visitorsData = new VisitorsData();

    /**
     * Конструктор класса
     */
    public VisitorsUpdater(Label uniqueLabel, Label totalLabel) {
        this.uniqueLabel = uniqueLabel;
        this.totalLabel = totalLabel;
    }

    /**
     * Обработчик нажатия на кнопку обновить. На данный момент копка отсутсвует, но обработчик используется
     * при перезагрузке страницы
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {
        final WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        upsertAddress(webBrowser.getAddress());
        updateVisitors();
        uniqueLabel.setValue(Integer.toString(visitorsData.unique));
        totalLabel.setValue(Integer.toString(visitorsData.total));
    }

    /** Обновление информации при запуске класса как Runnable */
    @Override
    public void run() {
        buttonClick(null);
    }

    /**
     * Загрузить данные из базы данных
     */
    private void updateVisitors() {
        VisitorsDataService loader = new MongoVisitorsDataService();
        try {
            visitorsData = loader.getData();
        } catch (ExceptionForUser e) {
            Notification.show(e.what(), Notification.Type.ERROR_MESSAGE);
        }
    }

    /**
     * Сохранить IP-адрес в базе данных
     * @param ip IP-адрес
     */
    private void upsertAddress(String ip) {
        try {
            new MongoVisitorsDataService().registerIP(ip);
        } catch (ExceptionForUser e) {
            Notification.show(e.what(), Notification.Type.ERROR_MESSAGE);
        }
    }
}
