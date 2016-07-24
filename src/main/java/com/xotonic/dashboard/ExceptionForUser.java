
package com.xotonic.dashboard;

/**
 * Исключение, которое следует показать клиенту
 * @author xotonic
 */
public class ExceptionForUser extends Exception {
    private String what;
    public String what() {
        return what;
    }
    public ExceptionForUser(String what)
    {
        this.what = what;
    }
}
