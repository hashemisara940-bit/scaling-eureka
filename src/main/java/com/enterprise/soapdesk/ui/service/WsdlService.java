package com.enterprise.soapdesk.ui.service;

import com.enterprise.soapdesk.ui.model.WsdlOperation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WsdlService {

    public List<WsdlOperation> parseWsdl(Path path) throws Exception {
        try (InputStream in = Files.newInputStream(path)) {
            return parse(in);
        }
    }

    public List<WsdlOperation> parseWsdl(String wsdlUrl) throws Exception {
        try (InputStream in = URI.create(wsdlUrl).toURL().openStream()) {
            return parse(in);
        }
    }

    private List<WsdlOperation> parse(InputStream in) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        NodeList portTypes = doc.getElementsByTagNameNS("*", "portType");
        List<WsdlOperation> operations = new ArrayList<>();
        for (int i = 0; i < portTypes.getLength(); i++) {
            Element portType = (Element) portTypes.item(i);
            String portName = portType.getAttribute("name");
            NodeList opNodes = portType.getElementsByTagNameNS("*", "operation");
            for (int j = 0; j < opNodes.getLength(); j++) {
                Element op = (Element) opNodes.item(j);
                String opName = op.getAttribute("name");
                String inputName = "request";
                NodeList inputs = op.getElementsByTagNameNS("*", "input");
                if (inputs.getLength() > 0) {
                    Element input = (Element) inputs.item(0);
                    inputName = input.getAttribute("name").isBlank() ? "request" : input.getAttribute("name");
                }
                operations.add(new WsdlOperation("ImportedService", portName, opName, inputName));
            }
        }
        return operations;
    }

    public String generateSoapTemplate(WsdlOperation operation) {
        return """
                <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">
                  <soapenv:Header/>
                  <soapenv:Body>
                    <%s>
                      <!-- TODO add parameters -->
                    </%s>
                  </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(operation.operationName(), operation.operationName());
    }

    public String executeSoap(String endpoint, String soapAction, String xmlBody) {
        var client = java.net.http.HttpClient.newHttpClient();
        var request = java.net.http.HttpRequest.newBuilder(java.net.URI.create(endpoint))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", soapAction)
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(xmlBody))
                .build();
        try {
            var response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception ex) {
            return "<error>" + ex.getMessage() + "</error>";
        }
    }
}
