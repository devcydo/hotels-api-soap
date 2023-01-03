package com.example.hotelsapisoap.endpoint;

import com.example.hotelsapisoap.exception.NotFoundException;
import com.example.hotelsapisoap.exception.ServiceFaultException;
import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.service.AmenityService;

import com.hotels.soap.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Set;

@Endpoint
public class AmenityEndpoint {
    private static final String NAMESPACE_URI = "http://hotels.com/soap";

    private AmenityService amenityService;

    @Autowired
    public AmenityEndpoint(AmenityService amenityService) { this.amenityService = amenityService; }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllAmenityDetailsRequest")
    @ResponsePayload
    public GetAllAmenityDetailsResponse processAllAmenityDetailsRequest(@RequestPayload GetAllAmenityDetailsRequest request) throws NotFoundException {
        Set<Amenity> amenities = amenityService.getAll();

        return mapAllAmenityDetailsResponse(amenities);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllAmenityDetailsByHotelRequest")
    @ResponsePayload
    public GetAllAmenityDetailsByHotelResponse getAllAmenityDetailsByHotelRequest(@RequestPayload GetAllAmenityDetailsByHotelRequest request) throws NotFoundException {
        Set<Amenity> amenities = amenityService.getByHotel(request.getIdHotel());

        if(amenities == null) throw new NotFoundException("Amenities for hotel with id " + request.getIdHotel() + " not found");

        return mapAllAmenityDetailsByHotelResponse(amenities);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SaveAmenityDetailsRequest")
    @ResponsePayload
    public SaveAmenityDetailsResponse saveAmenityDetailsRequest(@RequestPayload SaveAmenityDetailsRequest request){
        Amenity amenity = amenityService.save(toAmenity(request.getAmenityDetails()));
        return mapSaveAmenityDetails(amenity);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteAmenityDetailsRequest")
    @ResponsePayload
    public DeleteAmenityDetailsResponse deleteAmenityDetailsRequest(@RequestPayload DeleteAmenityDetailsRequest request) throws ServiceFaultException {
        boolean deleted = amenityService.deleteById(request.getId());

        return mapDeleteAmenityDetails(deleted);
    }

    private GetAllAmenityDetailsResponse mapAllAmenityDetailsResponse(Set<Amenity> amenities) {
        GetAllAmenityDetailsResponse response = new GetAllAmenityDetailsResponse();

        amenities.stream().forEach(amenity -> response.getAmenityDetails().add(toAmenityDetails(amenity)));

        return response;
    }

    private GetAllAmenityDetailsByHotelResponse mapAllAmenityDetailsByHotelResponse(Set<Amenity> amenities) {
        GetAllAmenityDetailsByHotelResponse response = new GetAllAmenityDetailsByHotelResponse();

        amenities.stream().forEach(amenity -> response.getAmenityDetails().add(toAmenityDetails(amenity)));

        return response;
    }

    private SaveAmenityDetailsResponse mapSaveAmenityDetails(Amenity amenity) {
        SaveAmenityDetailsResponse response = new SaveAmenityDetailsResponse();

        response.setAmenityDetails(toAmenityDetails(amenity));

        return response;
    }

    private DeleteAmenityDetailsResponse mapDeleteAmenityDetails(boolean deleted) {
        DeleteAmenityDetailsResponse response = new DeleteAmenityDetailsResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        if (deleted) {
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content deleted successfully");
        } else {
            serviceStatus.setStatusCode("ERROR");
            serviceStatus.setMessage("Exception while deleting amenity");
        }

        response.setServiceStatus(serviceStatus);

        return response;
    }

    private AmenityDetails toAmenityDetails(Amenity amenity) {
        AmenityDetails amenityDetails = new AmenityDetails();
        BeanUtils.copyProperties(amenity, amenityDetails);
        return amenityDetails;
    }

    private Amenity toAmenity(AmenityDetails amenityDetails) {
        Amenity amenity = new Amenity();

        if(amenityDetails.getId() != 0) amenity.setId(amenityDetails.getId());
        amenity.setName(amenityDetails.getName());
        amenity.setDescription(amenityDetails.getDescription());

        return amenity;
    }
}
