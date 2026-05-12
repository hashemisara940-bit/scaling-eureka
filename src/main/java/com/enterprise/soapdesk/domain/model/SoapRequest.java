package com.enterprise.soapdesk.domain.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "soap_requests")
public class SoapRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String endpointUrl;
    private String soapAction;
    @Lob
    private String requestXml;
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
    public String getSoapAction() { return soapAction; }
    public void setSoapAction(String soapAction) { this.soapAction = soapAction; }
    public String getRequestXml() { return requestXml; }
    public void setRequestXml(String requestXml) { this.requestXml = requestXml; }
}
