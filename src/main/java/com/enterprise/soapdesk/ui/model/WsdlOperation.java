package com.enterprise.soapdesk.ui.model;

public record WsdlOperation(String serviceName, String portName, String operationName, String inputName) {
}
