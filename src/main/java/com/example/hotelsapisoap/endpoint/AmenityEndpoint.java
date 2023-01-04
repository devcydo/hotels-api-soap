package com.example.hotelsapisoap.endpoint;

import com.example.hotelsapisoap.exception.BadRequestException;
import com.example.hotelsapisoap.exception.NotFoundException;
import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.service.AmenityService;

import static com.example.hotelsapisoap.helper.AmenityHelper.*;

import com.hotels.soap.*;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Set;

@Endpoint
public class AmenityEndpoint {
    private static final String NAMESPACE_URI = "http://hotels.com/soap";

    private final AmenityService amenityService;

    public AmenityEndpoint(AmenityService amenityService) { this.amenityService = amenityService; }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllAmenityDetailsRequest")
    @ResponsePayload
    public GetAllAmenityDetailsResponse processAllAmenityDetailsRequest(@RequestPayload GetAllAmenityDetailsRequest request) throws NotFoundException {
        Set<Amenity> amenities = amenityService.getAmenities();
        return mapAllAmenityDetailsResponse(amenities);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllAmenityDetailsByHotelRequest")
    @ResponsePayload
    public GetAllAmenityDetailsByHotelResponse getAllAmenityDetailsByHotelRequest(@RequestPayload GetAllAmenityDetailsByHotelRequest request) throws NotFoundException {
        Set<Amenity> amenities = amenityService.getByHotel(request.getIdHotel());
        return mapAllAmenityDetailsByHotelResponse(amenities);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddAmenityDetailsRequest")
    @ResponsePayload
    public AddAmenityDetailsResponse addAmenityDetailsRequest(@RequestPayload AddAmenityDetailsRequest request) throws NotFoundException, BadRequestException {
        Amenity amenity = amenityService.addAmenity(toAmenity(request.getAmenityDetails()));
        return mapAddAmenityDetails(amenity);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "EditAmenityDetailsRequest")
    @ResponsePayload
    public EditAmenityDetailsResponse editAmenityDetailsRequest(@RequestPayload EditAmenityDetailsRequest request) throws NotFoundException, BadRequestException {
        Amenity amenity = amenityService.editAmenity(toAmenity(request.getAmenityDetails()));
        return mapEditAmenityDetails(amenity);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteAmenityDetailsRequest")
    @ResponsePayload
    public DeleteAmenityDetailsResponse deleteAmenityDetailsRequest(@RequestPayload DeleteAmenityDetailsRequest request) throws NotFoundException {
        amenityService.deleteById(request.getId());
        return mapDeleteAmenityDetails();
    }
}
