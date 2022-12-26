package com.example.localhost.exception;

import com.hotels.hotels.ServiceStatus;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "{" + NotFoundException.NAMESPACE_URI + "}NOT_FOUND")
public class NotFoundException extends RuntimeException {

    public static final String NAMESPACE_URI = "http://hotels.com/exception";

    public NotFoundException(String message) {
        super(message);
    }

}
