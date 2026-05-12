package com.enterprise.soapdesk.infrastructure.soap;

import com.enterprise.soapdesk.domain.service.SoapExecutionService;
import com.enterprise.soapdesk.shared.dto.SoapExecutionResult;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;

@Service
public class SpringWsSoapExecutionService implements SoapExecutionService {
    private final WebServiceTemplate webServiceTemplate;

    public SpringWsSoapExecutionService(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    @Override
    public SoapExecutionResult execute(String endpoint, String soapAction, String xmlPayload) {
        Instant start = Instant.now();
        StringWriter out = new StringWriter();
        webServiceTemplate.sendSourceAndReceiveToResult(endpoint, new StringSource(xmlPayload), new StreamResult(out));
        long millis = Duration.between(start, Instant.now()).toMillis();
        return new SoapExecutionResult(200, millis, out.toString(), false, null);
    }
}
