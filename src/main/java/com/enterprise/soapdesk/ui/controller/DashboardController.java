package com.enterprise.soapdesk.ui.controller;

import com.enterprise.soapdesk.domain.service.SoapExecutionService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

@Component
public class DashboardController {
    @FXML private TextField endpointField;
    @FXML private TextField actionField;
    @FXML private TextArea requestEditor;
    @FXML private TextArea responseViewer;
    @FXML private Label metricsLabel;

    private final SoapExecutionService service;

    public DashboardController(SoapExecutionService service) {
        this.service = service;
    }

    @FXML
    public void onExecute() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                var result = service.execute(endpointField.getText(), actionField.getText(), requestEditor.getText());
                responseViewer.setText(result.rawResponse());
                metricsLabel.setText("Status: " + result.httpStatus() + " | " + result.responseTimeMs() + " ms");
                return null;
            }
        };
        new Thread(task, "soap-exec-thread").start();
    }
}
