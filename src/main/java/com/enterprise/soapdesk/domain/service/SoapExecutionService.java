package com.enterprise.soapdesk.domain.service;

import com.enterprise.soapdesk.shared.dto.SoapExecutionResult;

public interface SoapExecutionService {
    SoapExecutionResult execute(String endpoint, String soapAction, String xmlPayload);
}
