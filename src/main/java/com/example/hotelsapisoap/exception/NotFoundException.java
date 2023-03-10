package com.example.hotelsapisoap.exception;

import com.hotels.soap.ServiceStatus;

public class NotFoundException extends RuntimeException {

    private ServiceStatus serviceStatus;

    public NotFoundException(String message) {
        super(message);
        this.serviceStatus = new ServiceStatus();
        serviceStatus.setStatusCode("NOT_FOUND");
        serviceStatus.setMessage(message);
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
