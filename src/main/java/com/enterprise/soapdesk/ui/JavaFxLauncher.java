package com.enterprise.soapdesk.ui;

import com.enterprise.soapdesk.application.SoapDeskApplication;
import javafx.application.Application;

public final class JavaFxLauncher {
    private JavaFxLauncher() {}

    public static void main(String[] args) {
        Application.launch(SoapDeskApplication.class, args);
    }
}
