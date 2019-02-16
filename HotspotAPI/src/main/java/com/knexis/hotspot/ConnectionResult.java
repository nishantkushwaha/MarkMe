package com.knexis.hotspot;

/**
 * Created by Nana Kwame Nyantakyi on 15/01/2018.
 * Purpose:
 */

public class ConnectionResult {
    private String message;
    private boolean successful;

    public ConnectionResult(String message, boolean successful) {
        this.message = message;
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
