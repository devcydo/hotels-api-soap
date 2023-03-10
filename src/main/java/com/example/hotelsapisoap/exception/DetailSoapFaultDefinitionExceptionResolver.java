package com.example.hotelsapisoap.exception;

import javax.xml.namespace.QName;

import com.hotels.soap.ServiceStatus;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("statusCode");
    private static final QName MESSAGE = new QName("message");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof NotFoundException) {
            ServiceStatus status = ((NotFoundException) ex).getServiceStatus();
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(CODE).addText(status.getStatusCode());
            detail.addFaultDetailElement(MESSAGE).addText(status.getMessage());
        } else if (ex instanceof BadRequestException) {
            ServiceStatus status = ((BadRequestException) ex).getServiceStatus();
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(CODE).addText(status.getStatusCode());
            detail.addFaultDetailElement(MESSAGE).addText(status.getMessage());
        }
    }
}
