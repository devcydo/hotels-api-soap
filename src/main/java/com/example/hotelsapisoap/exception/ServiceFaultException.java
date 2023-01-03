package com.example.hotelsapisoap.exception;

import com.hotels.soap.ServiceStatus;

public class ServiceFaultException extends RuntimeException {
    private ServiceStatus serviceStatus;

    public ServiceFaultException(String message, ServiceStatus serviceStatus) {
        super(message);
        this.serviceStatus = serviceStatus;
    }

    public ServiceFaultException(String message, Throwable e, ServiceStatus serviceStatus) {
        super(message);
        this.serviceStatus = serviceStatus;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
