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
public class MongoVisitorsLoader implements VisitorsLoader {

    @Override
    public VisitorsData getData() {
        VisitorsData data = new VisitorsData();
        data.total = 100;
        data.unique = 90;
        
        return data;
    }

    @Override
    public void registerIP(String ip) {
    }
    
}
