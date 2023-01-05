package com.example.hotelsapisoap.helper;

import com.example.hotelsapisoap.exception.BadRequestException;
import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.model.Hotel;
import com.hotels.soap.*;
import org.springframework.beans.BeanUtils;

import java.util.Set;

public class AmenityHelper {
    public static GetAllAmenityDetailsResponse mapAllAmenityDetailsResponse(Set<Amenity> amenities) {
        GetAllAmenityDetailsResponse response = new GetAllAmenityDetailsResponse();

        amenities.forEach(amenity -> response.getAmenityDetails().add(toAmenityDetails(amenity)));

        return response;
    }

    public static GetAllAmenityDetailsByHotelResponse mapAllAmenityDetailsByHotelResponse(Set<Amenity> amenities) {
        GetAllAmenityDetailsByHotelResponse response = new GetAllAmenityDetailsByHotelResponse();

        amenities.forEach(amenity -> response.getAmenityDetails().add(toAmenityDetails(amenity)));

        return response;
    }

    public static AddAmenityDetailsResponse mapAddAmenityDetails(Amenity amenity) {
        AddAmenityDetailsResponse response = new AddAmenityDetailsResponse();

        response.setAmenityDetails(toAmenityDetails(amenity));

        return response;
    }

    public static EditAmenityDetailsResponse mapEditAmenityDetails(Amenity amenity) {
        EditAmenityDetailsResponse response = new EditAmenityDetailsResponse();

        response.setAmenityDetails(toAmenityDetails(amenity));

        return response;
    }

    public static DeleteAmenityDetailsResponse mapDeleteAmenityDetails() {
        DeleteAmenityDetailsResponse response = new DeleteAmenityDetailsResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        serviceStatus.setStatusCode("SUCCESS");
        serviceStatus.setMessage("Content deleted successfully");
        response.setServiceStatus(serviceStatus);

        return response;
    }

    public static AmenityDetails toAmenityDetails(Amenity amenity) {
        AmenityDetails amenityDetails = new AmenityDetails();
        BeanUtils.copyProperties(amenity, amenityDetails);
        return amenityDetails;
    }

    public static Amenity toAmenity(AmenityDetails amenityDetails) {
        Amenity amenity = new Amenity();

        if(amenityDetails.getId() != 0) amenity.setId(amenityDetails.getId());
        amenity.setName(amenityDetails.getName());
        amenity.setDescription(amenityDetails.getDescription());

        return amenity;
    }

    public static void validateAmenity(Amenity amenity) {
        if(amenity.getName() == null || amenity.getName().equals(""))
            throw new BadRequestException("Name cannot be empty");

        if(amenity.getDescription() == null || amenity.getDescription().equals(""))
            throw new BadRequestException("Description cannot be empty");

    }
}
