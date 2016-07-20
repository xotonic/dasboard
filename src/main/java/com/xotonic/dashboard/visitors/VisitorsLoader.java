/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xotonic.dashboard.visitors;

/**
 *
 * @author xotonic
 */
public interface VisitorsLoader {
    public VisitorsData getData();
    public void registerIP(String ip);
}
