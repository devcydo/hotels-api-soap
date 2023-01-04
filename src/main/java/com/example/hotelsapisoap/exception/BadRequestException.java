package com.example.hotelsapisoap.exception;

import com.hotels.soap.ServiceStatus;

public class BadRequestException extends RuntimeException {
    private ServiceStatus serviceStatus;

    public BadRequestException(String message) {
        super("ERROR");
        this.serviceStatus = new ServiceStatus();
        serviceStatus.setStatusCode("BAD_REQUEST");
        serviceStatus.setMessage(message);
    }

    public ServiceStatus getServiceStatus() {
        return this.serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
