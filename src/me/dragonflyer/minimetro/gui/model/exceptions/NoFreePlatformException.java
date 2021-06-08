package me.dragonflyer.minimetro.gui.model.exceptions;

public class NoFreePlatformException extends Exception {

    public NoFreePlatformException() {
        super();
    }

    public NoFreePlatformException(String message) {
        super(message);
    }

    public NoFreePlatformException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFreePlatformException(Throwable cause) {
        super(cause);
    }

    protected NoFreePlatformException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
