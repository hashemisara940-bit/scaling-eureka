package com.enterprise.soapdesk.application;

import com.enterprise.soapdesk.ui.controller.MainController;
import com.enterprise.soapdesk.ui.service.WsdlService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SoapDeskApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/enterprise/soapdesk/ui/main-view.fxml"));
        loader.setControllerFactory(type -> {
            if (type.equals(MainController.class)) {
                return new MainController(new WsdlService());
            }
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Scene scene = new Scene(loader.load(), 1400, 920);
        scene.getStylesheets().add(getClass().getResource("/com/enterprise/soapdesk/ui/theme.css").toExternalForm());
        stage.setTitle("SoapDesk Enterprise");
        stage.setScene(scene);
        stage.show();
    }
}
