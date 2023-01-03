package com.example.hotelsapisoap.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "{" + NotFoundException.NAMESPACE_URI + "}ERROR_SAVING")
public class ErrorSavingException extends RuntimeException {
    public static final String NAMESPACE_URI = "http://hotels.com/exception";

    public ErrorSavingException(String message) {
        super(message);
    }
}
