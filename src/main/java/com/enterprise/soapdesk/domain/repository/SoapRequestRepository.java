package com.enterprise.soapdesk.domain.repository;

import com.enterprise.soapdesk.domain.model.SoapRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoapRequestRepository extends JpaRepository<SoapRequest, Long> { }
