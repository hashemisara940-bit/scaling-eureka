package com.enterprise.soapdesk.application;

import com.enterprise.soapdesk.ui.JavaFxLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.enterprise.soapdesk")
public class SoapDeskApplication {
    public static void main(String[] args) {
        JavaFxLauncher.launchApp(args);
    }
}
