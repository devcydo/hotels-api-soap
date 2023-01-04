package com.example.hotelsapisoap.endpoint;

import com.example.hotelsapisoap.exception.ErrorSavingException;
import com.example.hotelsapisoap.exception.NotFoundException;
import com.example.hotelsapisoap.exception.ServiceFaultException;
import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.model.Hotel;
import com.example.hotelsapisoap.service.HotelService;

import com.hotels.soap.*;

import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Endpoint
public class HotelEndpoint {

    private static final String NAMESPACE_URI = "http://hotels.com/soap";

    private final HotelService hotelService;

    public HotelEndpoint(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetHotelDetailsRequest")
    @ResponsePayload
    public GetHotelDetailsResponse processHotelDetailsRequest(@RequestPayload GetHotelDetailsRequest request) throws NotFoundException {
        Hotel hotel = hotelService.getById(request.getId());

        if(hotel == null) throw new NotFoundException("Hotel with id " + request.getId() + "not found");

        return mapHotelDetails(toHotelDetails(hotel));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllHotelDetailsRequest")
    @ResponsePayload
    public GetAllHotelDetailsResponse processAllHotelDetailsRequest(@RequestPayload GetAllHotelDetailsRequest request){
        Page<Hotel> hotelPage = hotelService.filterByName(Optional.of(request.getPageNumber()).orElse(0), Optional.ofNullable(request.getFilterByName()).orElse(""));
        return mapAllHotelDetails(hotelPage);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SaveHotelDetailsRequest")
    @ResponsePayload
    public SaveHotelDetailsResponse saveHotelDetailsRequest(@RequestPayload SaveHotelDetailsRequest request){
        Hotel hotel = hotelService.save(toHotel(request.getHotelDetails()));
        return mapSaveHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddAmenityDetailsToHotelDetailsRequest")
    @ResponsePayload
    public AddAmenityDetailsToHotelDetailsResponse addAmenityDetailsToHotelDetailsResponse(@RequestPayload AddAmenityDetailsToHotelDetailsRequest request) throws NotFoundException {
        Hotel hotel = hotelService.addAmenity(request.getIdHotel(), request.getIdAmenity());

        if(hotel == null) throw new ErrorSavingException("Error adding amenity to Hotel.");

        return mapAddAmenityDetailsToHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RemoveAmenityDetailsToHotelDetailsRequest")
    @ResponsePayload
    public RemoveAmenityDetailsToHotelDetailsResponse removeAmenityDetailsToHotelDetailsResponse(@RequestPayload RemoveAmenityDetailsToHotelDetailsRequest request){
        Hotel hotel = hotelService.removeAmenity(request.getIdHotel(), request.getIdAmenity());
        return mapRemoveAmenityDetailsToHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteHotelDetailsRequest")
    @ResponsePayload
    public DeleteHotelDetailsResponse deleteHotelDetailsRequest(@RequestPayload DeleteHotelDetailsRequest request) throws ServiceFaultException {
        boolean deleted = hotelService.deleteById(request.getId());

        return mapDeleteHotelDetails(deleted);
    }

    private GetHotelDetailsResponse mapHotelDetails(HotelDetails hotelDetails) {
        GetHotelDetailsResponse response = new GetHotelDetailsResponse();

        response.setHotelDetails(hotelDetails);

        return response;
    }

    private GetAllHotelDetailsResponse mapAllHotelDetails(Page<Hotel> hotelPage) {
        GetAllHotelDetailsResponse response = new GetAllHotelDetailsResponse();

        List<Hotel> hotels = new ArrayList<>();

        hotelPage.forEach(hotels::add);
        hotels.forEach(hotel -> response.getHotelDetails().add(toHotelDetails(hotel)));

        return response;
    }

    private SaveHotelDetailsResponse mapSaveHotelDetails(Hotel hotel) {
        SaveHotelDetailsResponse response = new SaveHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    private AddAmenityDetailsToHotelDetailsResponse mapAddAmenityDetailsToHotelDetails(Hotel hotel) {
        AddAmenityDetailsToHotelDetailsResponse response = new AddAmenityDetailsToHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    private RemoveAmenityDetailsToHotelDetailsResponse mapRemoveAmenityDetailsToHotelDetails(Hotel hotel) {
        RemoveAmenityDetailsToHotelDetailsResponse response = new RemoveAmenityDetailsToHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    private DeleteHotelDetailsResponse mapDeleteHotelDetails(boolean deleted) {
        DeleteHotelDetailsResponse response = new DeleteHotelDetailsResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        if (deleted) {
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content deleted successfully");
        } else {
            serviceStatus.setStatusCode("ERROR");
            serviceStatus.setMessage("Exception while deleting hotel");
            serviceStatus.setMessage("Exception while deleting hotel");
        }

        response.setServiceStatus(serviceStatus);

        return response;
    }

    private HotelDetails toHotelDetails(Hotel hotel) {
        HotelDetails hotelDetails = new HotelDetails();

        hotelDetails.setId(hotel.getId());
        hotelDetails.setName(hotel.getName());
        hotelDetails.setAddress(hotel.getAddress());
        hotelDetails.setRating(hotel.getRating());

        Optional.ofNullable(hotel.getAmenities())
                .orElseGet(Collections::emptySet)
                .stream().forEach(amenity -> hotelDetails.getAmenityDetails().add(toAmenityDetails(amenity)));

        return hotelDetails;
    }

    private AmenityDetails toAmenityDetails(Amenity amenity) {
        AmenityDetails amenityDetails = new AmenityDetails();

        amenityDetails.setId(amenity.getId());
        amenityDetails.setName(amenity.getName());
        amenityDetails.setDescription(amenity.getDescription());

        return amenityDetails;
    }

    private Hotel toHotel(HotelDetails hotelDetails) {
        Hotel hotel = new Hotel();
        if (hotelDetails.getId() != 0) hotel.setId(hotelDetails.getId());
        hotel.setName(hotelDetails.getName());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setRating(hotelDetails.getRating());
        return hotel;
    }

}
