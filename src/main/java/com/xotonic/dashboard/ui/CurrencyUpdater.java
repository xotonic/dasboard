/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xotonic.dashboard.ui;

import com.vaadin.ui.*;
import com.xotonic.dashboard.ExceptionForUser;
import com.xotonic.dashboard.currency.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Обновление курса валют<br>
 * @author xotonic
 */
public class CurrencyUpdater implements Button.ClickListener, Runnable {
    /**
     * Надпись для значения текущего курса доллара
     */
    private final Label usdLabel;
    /**
     * Надпись для значения изменения текущего курса доллара
     */
    private final Label usdDeltaLabel;
    /**
     * Надпись для значения текущего курса евро
     */
    private final Label eurLabel;
    /**
     * Надпись для значения изменения текущего курса евро
     */
    private final Label eurDeltaLabel;
    /**
     * Метка, отключающая вывод сообщения пользователю об успешном обновлении
     */
    private boolean silent;
    /**
     * Надпись с временем последнего обновления
     */
    private final Label timeStatusValueLabel;

    /** Форматирование для строк с числовыми значениями курса */
    private final DecimalFormat currencyFormat;
    /** Форматирование для строк с числовыми значениями изменения курса */
    private final DecimalFormat currencyDeltaFormat;

    /**
     * Информация о текущем курсе
     */
    private CurrencyData currencyData = new CurrencyData();
    /**  Конструктор
     * @param usdLabel курс доллара
     * @param usdDeltaLabel изменение курса доллара
     * @param eurLabel курс евро
     * @param eurDeltaLabel изменение курса евро
     * @param timeStatusValueLabel дата и время, которое обновится после получения
     * новых данных
     */
    public CurrencyUpdater(
            Label usdLabel,
            Label usdDeltaLabel,
            Label eurLabel,
            Label eurDeltaLabel,
            Label timeStatusValueLabel)
            {
        this.usdLabel = usdLabel;
        this.usdDeltaLabel = usdDeltaLabel;
        this.eurLabel = eurLabel;
        this.eurDeltaLabel = eurDeltaLabel;
        this.timeStatusValueLabel = timeStatusValueLabel;

        this.silent = true;
        
        currencyFormat = new DecimalFormat("#.####");
        currencyFormat.setRoundingMode(RoundingMode.CEILING);
        currencyDeltaFormat = new DecimalFormat("+#.###;-#.###");
        currencyDeltaFormat.setRoundingMode(RoundingMode.CEILING);
    }

    /**
     * Обновить информацию о курсе валют
     */
    private void updateCurrency() {
        CurrencyDataService loader = new CBRDataService();
        try {
            currencyData = loader.getData();
        } catch (ExceptionForUser e) {
            Notification.show(e.what(), Notification.Type.ERROR_MESSAGE);
        }
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
     * Обработчик клика по кнопке 'Обновить'
     * @param event
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {
        updateCurrency();
        usdLabel.setValue(currencyFormat.format(currencyData.USD));
        usdDeltaLabel.setValue(currencyDeltaFormat.format(currencyData.USDDelta));
        usdDeltaLabel.setStyleName(currencyData.USDDelta < 0
                ? "currency-delta-negative" : "currency-delta-positive", true);

        eurLabel.setValue(currencyFormat.format(currencyData.EUR));
        eurDeltaLabel.setValue(currencyDeltaFormat.format(currencyData.EURDelta));
        eurDeltaLabel.setStyleName(currencyData.EURDelta < 0
                ? "currency-delta-negative" : "currency-delta-positive", true);
        if (silent == false) {
            Notification.show("Валюта обновлена", Notification.Type.HUMANIZED_MESSAGE);
        }
        updateDateLabel();
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
