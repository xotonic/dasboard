/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xotonic.dashboard.visitors;

import com.xotonic.dashboard.ExceptionForUser;

/**
 *
 * @author xotonic
 */
public interface VisitorsDataService {
    VisitorsData getData() throws ExceptionForUser;
    void registerIP(String ip) throws ExceptionForUser ;
}
