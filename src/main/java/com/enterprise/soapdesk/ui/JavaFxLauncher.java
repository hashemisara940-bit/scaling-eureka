package com.enterprise.soapdesk.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxLauncher extends Application {

    private ConfigurableApplicationContext context;

    public static void launchApp(String[] args) {
        Application.launch(JavaFxLauncher.class, args);
    }

    @Override
    public void init() {
        context = new SpringApplicationBuilder(com.enterprise.soapdesk.application.SoapDeskApplication.class)
                .headless(false)
                .run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("/css/theme-dark.css").toExternalForm());
        stage.setTitle("Enterprise SOAP Testing Desktop");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        context.close();
    }
}
