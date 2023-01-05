package com.example.hotelsapisoap.helper;

import com.example.hotelsapisoap.exception.BadRequestException;
import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.model.Hotel;
import com.hotels.soap.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HotelHelper {
    public static GetHotelDetailsResponse mapHotelDetails(HotelDetails hotelDetails) {
        GetHotelDetailsResponse response = new GetHotelDetailsResponse();

        response.setHotelDetails(hotelDetails);

        return response;
    }

    public static GetAllHotelDetailsResponse mapAllHotelDetails(Page<Hotel> hotelPage) {
        GetAllHotelDetailsResponse response = new GetAllHotelDetailsResponse();

        List<Hotel> hotels = new ArrayList<>();

        hotelPage.forEach(hotels::add);
        hotels.forEach(hotel -> response.getHotelDetails().add(toHotelDetails(hotel)));

        return response;
    }

    public static AddHotelDetailsResponse mapAddHotelDetails(Hotel hotel) {
        AddHotelDetailsResponse response = new AddHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    public static EditHotelDetailsResponse mapEditHotelDetails(Hotel hotel) {
        EditHotelDetailsResponse response = new EditHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    public static AddAmenityDetailsToHotelDetailsResponse mapAddAmenityDetailsToHotelDetails(Hotel hotel) {
        AddAmenityDetailsToHotelDetailsResponse response = new AddAmenityDetailsToHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    public static RemoveAmenityDetailsToHotelDetailsResponse mapRemoveAmenityDetailsToHotelDetails(Hotel hotel) {
        RemoveAmenityDetailsToHotelDetailsResponse response = new RemoveAmenityDetailsToHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    public static DeleteHotelDetailsResponse mapDeleteHotelDetails() {
        DeleteHotelDetailsResponse response = new DeleteHotelDetailsResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        serviceStatus.setStatusCode("SUCCESS");
        serviceStatus.setMessage("Content deleted successfully");
        response.setServiceStatus(serviceStatus);

        return response;
    }

    public static HotelDetails toHotelDetails(Hotel hotel) {
        HotelDetails hotelDetails = new HotelDetails();

        hotelDetails.setId(hotel.getId());
        hotelDetails.setName(hotel.getName());
        hotelDetails.setAddress(hotel.getAddress());
        hotelDetails.setRating(hotel.getRating());

        Optional.ofNullable(hotel.getAmenities())
                .orElseGet(Collections::emptySet)
                .forEach(amenity -> hotelDetails.getAmenityDetails().add(toAmenityDetails(amenity)));

        return hotelDetails;
    }

    public static AmenityDetails toAmenityDetails(Amenity amenity) {
        AmenityDetails amenityDetails = new AmenityDetails();

        amenityDetails.setId(amenity.getId());
        amenityDetails.setName(amenity.getName());
        amenityDetails.setDescription(amenity.getDescription());

        return amenityDetails;
    }

    public static Hotel toHotel(HotelDetails hotelDetails) {
        Hotel hotel = new Hotel();
        if (hotelDetails.getId() != 0) hotel.setId(hotelDetails.getId());
        hotel.setName(hotelDetails.getName());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setRating(hotelDetails.getRating());
        return hotel;
    }

    public static void validateHotel(Hotel hotel) {
        if(hotel.getName() == null || hotel.getName().equals(""))
            throw new BadRequestException("Name cannot be empty");

        if(hotel.getAddress() == null || hotel.getAddress().equals(""))
            throw new BadRequestException("Address cannot be empty");

        if(hotel.getRating() < 1 || hotel.getRating() > 5)
            throw new BadRequestException("Invalid value for rating. Provide an integer value from 1 to 5;");
    }
}
