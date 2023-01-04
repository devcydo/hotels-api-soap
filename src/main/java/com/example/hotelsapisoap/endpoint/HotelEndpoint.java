package com.example.hotelsapisoap.endpoint;

import com.example.hotelsapisoap.exception.BadRequestException;
import com.example.hotelsapisoap.exception.NotFoundException;
import com.example.hotelsapisoap.model.Hotel;
import com.example.hotelsapisoap.service.HotelService;

import static com.example.hotelsapisoap.helper.HotelHelper.*;

import com.hotels.soap.*;

import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

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
        return mapHotelDetails(toHotelDetails(hotel));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllHotelDetailsRequest")
    @ResponsePayload
    public GetAllHotelDetailsResponse processAllHotelDetailsRequest(@RequestPayload GetAllHotelDetailsRequest request) throws NotFoundException {
        Page<Hotel> hotelPage = hotelService.getHotels(Optional.of(request.getPageNumber()).orElse(0), Optional.ofNullable(request.getFilterByName()).orElse(""));
        return mapAllHotelDetails(hotelPage);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddHotelDetailsRequest")
    @ResponsePayload
    public AddHotelDetailsResponse addHotelDetailsRequest(@RequestPayload AddHotelDetailsRequest request) throws NotFoundException, BadRequestException {
        Hotel hotel = hotelService.addHotel(toHotel(request.getHotelDetails()));
        return mapAddHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "EditHotelDetailsRequest")
    @ResponsePayload
    public EditHotelDetailsResponse editHotelDetailsRequest(@RequestPayload EditHotelDetailsRequest request) throws NotFoundException, BadRequestException {
        Hotel hotel = hotelService.editHotel(toHotel(request.getHotelDetails()));
        return mapEditHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddAmenityDetailsToHotelDetailsRequest")
    @ResponsePayload
    public AddAmenityDetailsToHotelDetailsResponse addAmenityDetailsToHotelDetailsResponse(@RequestPayload AddAmenityDetailsToHotelDetailsRequest request) throws NotFoundException {
        Hotel hotel = hotelService.addAmenity(request.getIdHotel(), request.getIdAmenity());
        return mapAddAmenityDetailsToHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RemoveAmenityDetailsToHotelDetailsRequest")
    @ResponsePayload
    public RemoveAmenityDetailsToHotelDetailsResponse removeAmenityDetailsToHotelDetailsResponse(@RequestPayload RemoveAmenityDetailsToHotelDetailsRequest request) throws NotFoundException{
        Hotel hotel = hotelService.removeAmenity(request.getIdHotel(), request.getIdAmenity());
        return mapRemoveAmenityDetailsToHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteHotelDetailsRequest")
    @ResponsePayload
    public DeleteHotelDetailsResponse deleteHotelDetailsRequest(@RequestPayload DeleteHotelDetailsRequest request) throws NotFoundException {
        hotelService.deleteById(request.getId());
        return mapDeleteHotelDetails();
    }
}
