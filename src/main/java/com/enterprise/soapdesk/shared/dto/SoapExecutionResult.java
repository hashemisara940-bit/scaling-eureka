package com.enterprise.soapdesk.shared.dto;

public record SoapExecutionResult(int httpStatus, long responseTimeMs, String rawResponse,
                                  boolean fault, String faultMessage) { }
