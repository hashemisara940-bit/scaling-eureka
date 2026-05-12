package com.enterprise.soapdesk.ui.controller;

import com.enterprise.soapdesk.ui.model.WsdlOperation;
import com.enterprise.soapdesk.ui.service.WsdlService;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.time.Instant;
import java.util.List;

public class MainController {
    @FXML private VBox sidebar;
    @FXML private Label statusLabel;
    @FXML private Label metadataLabel;
    @FXML private TextField endpointField;
    @FXML private TextField soapActionField;
    @FXML private TreeView<String> wsdlTree;
    @FXML private ProgressIndicator wsdlProgress;
    @FXML private CodeArea requestArea;
    @FXML private CodeArea responseArea;

    private boolean collapsed;
    private final WsdlService wsdlService;

    public MainController(WsdlService wsdlService) { this.wsdlService = wsdlService; }

    @FXML
    public void initialize() {
        endpointField.setText("http://www.dneonline.com/calculator.asmx");
        soapActionField.setText("http://tempuri.org/Add");
        requestArea.replaceText("""
                <soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">
                  <soap:Body>
                    <Add xmlns=\"http://tempuri.org/\">
                      <intA>15</intA>
                      <intB>27</intB>
                    </Add>
                  </soap:Body>
                </soap:Envelope>
                """);
        responseArea.replaceText("Click Execute to run the first live SOAP request.");
        wsdlTree.setRoot(new TreeItem<>("WSDL Imports"));
        wsdlTree.getRoot().setExpanded(true);
    }

    @FXML
    void toggleSidebar() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), sidebar);
        tt.setToX(collapsed ? 0 : -170);
        tt.play();
        collapsed = !collapsed;
    }

    @FXML
    void executeSoap() {
        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return wsdlService.executeSoap(endpointField.getText(), soapActionField.getText(), requestArea.getText());
            }
        };
        task.setOnSucceeded(evt -> {
            responseArea.replaceText(task.getValue());
            metadataLabel.setText("Status: Completed | Time: " + Instant.now() + " | Length: " + task.getValue().length());
        });
        new Thread(task, "soap-exec").start();
    }

    @FXML
    void importWsdlFile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WSDL Files", "*.wsdl", "*.xml"));
        File file = chooser.showOpenDialog(sidebar.getScene().getWindow());
        if (file != null) {
            importWsdl(() -> wsdlService.parseWsdl(file.toPath()));
        }
    }

    @FXML
    void importWsdlUrl() {
        TextInputDialog dialog = new TextInputDialog("http://www.dneonline.com/calculator.asmx?wsdl");
        dialog.setHeaderText("Import WSDL from URL");
        dialog.showAndWait().ifPresent(url -> importWsdl(() -> wsdlService.parseWsdl(url)));
    }

    private void importWsdl(ThrowingSupplier<List<WsdlOperation>> supplier) {
        wsdlProgress.setVisible(true);
        statusLabel.setText("Parsing WSDL...");
        Task<List<WsdlOperation>> task = new Task<>() {
            @Override protected List<WsdlOperation> call() throws Exception { return supplier.get(); }
        };
        task.setOnSucceeded(evt -> {
            wsdlProgress.setVisible(false);
            populateTree(task.getValue());
            statusLabel.setText("WSDL imported: " + task.getValue().size() + " operations");
        });
        task.setOnFailed(evt -> {
            wsdlProgress.setVisible(false);
            statusLabel.setText("Failed: " + task.getException().getMessage());
        });
        new Thread(task, "wsdl-import").start();
    }

    private void populateTree(List<WsdlOperation> operations) {
        TreeItem<String> root = new TreeItem<>("WSDL Imports");
        for (WsdlOperation op : operations) {
            TreeItem<String> item = new TreeItem<>(op.portName() + " :: " + op.operationName());
            item.addEventHandler(TreeItem.branchExpandedEvent(), e -> {});
            root.getChildren().add(item);
        }
        wsdlTree.setRoot(root);
        wsdlTree.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected == null || selected == root) return;
            String[] parts = selected.getValue().split(" :: ");
            WsdlOperation op = new WsdlOperation("ImportedService", parts[0], parts[1], "request");
            requestArea.replaceText(wsdlService.generateSoapTemplate(op));
        });
        root.setExpanded(true);
    }

    @FXML void prettifyXml() { /* hook for formatter */ }
    @FXML void clearRequest() { requestArea.clear(); }
    @FXML void copyResponse() { responseArea.copy(); }

    @FunctionalInterface
    private interface ThrowingSupplier<T> { T get() throws Exception; }
}
