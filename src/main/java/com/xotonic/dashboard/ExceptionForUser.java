
package com.xotonic.dashboard;

/**
 * Исключение, которое следует показать клиенту
 * @author xotonic
 */
public class ExceptionForUser extends Exception {
    /**
     * Строковое описание ошибки
     */
    private String what;

    /**
     * Выдать строквое описание ошибки
     * @return
     */
    public String what() {
        return what;
    }

    /**
     *
     * @param what строковое описание ошибки
     */
    public ExceptionForUser(String what)
    {
        this.what = what;
    }
}
