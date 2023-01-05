package com.example.hotelsapisoap.integration;

import com.hotels.soap.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HotelsApiSoapIntegrationTest {
    private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

    @LocalServerPort
    private int port = 0;
    static Long createdHotelId = 0L;
    static Long createdAmenityId = 0L;

    @BeforeEach
    public void init() throws Exception {
        marshaller.setPackagesToScan("com.hotels.soap");
        marshaller.afterPropertiesSet();

        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        AddHotelDetailsRequest request = new AddHotelDetailsRequest();

        HotelDetails hotelDetails = new HotelDetails();
        hotelDetails.setName("Hotel from TEST 1");
        hotelDetails.setAddress("Address from Hotel TEST 1");
        hotelDetails.setRating(1);
        request.setHotelDetails(hotelDetails);

        Object response = ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        createdHotelId = ((AddHotelDetailsResponse) response).getHotelDetails().getId();
        assertNotNull(response);
    }

    @Test
    public void addHotel_badRequest(){
        Assertions.assertThrows(SoapFaultClientException.class, () -> {
           WebServiceTemplate ws = new WebServiceTemplate(marshaller);
           AddHotelDetailsRequest request = new AddHotelDetailsRequest();
           HotelDetails hotelDetails = new HotelDetails();
           hotelDetails.setName(null);
           hotelDetails.setAddress("Address");
           hotelDetails.setRating(1);
           request.setHotelDetails(hotelDetails);
           ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        });
    }

    @Test
    public void getHotelById_success() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        GetHotelDetailsRequest request = new GetHotelDetailsRequest();
        request.setId(createdHotelId);
        assertNotNull(ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request));
    }

    @Test
    public void getHotelById_notFound(){
        Assertions.assertThrows(SoapFaultClientException.class, () -> {
            WebServiceTemplate ws = new WebServiceTemplate(marshaller);
            GetHotelDetailsRequest request = new GetHotelDetailsRequest();
            request.setId(0);
            ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        });
    }

    @Test
    public void getHotels_success() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        GetAllHotelDetailsRequest request = new GetAllHotelDetailsRequest();
        request.setPageNumber(0);
        request.setFilterByName("");
        assertNotNull(ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request));
    }

    @Test
    public void editHotel_success() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        EditHotelDetailsRequest request = new EditHotelDetailsRequest();
        HotelDetails hotelDetails = new HotelDetails();
        hotelDetails.setId(createdHotelId);
        hotelDetails.setName("Hotel NiceView Mexico");
        hotelDetails.setAddress("Av. Principal, Colonia, Centro, CDMX.");
        hotelDetails.setRating(4);
        request.setHotelDetails(hotelDetails);
        Object response = ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        HotelDetails hotelResponse = ((EditHotelDetailsResponse) response).getHotelDetails();
        assertEquals(hotelDetails.getId(), hotelResponse.getId());
        assertEquals(hotelDetails.getName(), hotelResponse.getName());
        assertEquals(hotelDetails.getAddress(), hotelResponse.getAddress());
    }

    @Test
    public void editHotel_badRequest(){
        Assertions.assertThrows(SoapFaultClientException.class, () -> {
            WebServiceTemplate ws = new WebServiceTemplate(marshaller);
            EditHotelDetailsRequest request = new EditHotelDetailsRequest();
            HotelDetails hotelDetails = new HotelDetails();
            hotelDetails.setId(createdHotelId);
            hotelDetails.setName(null);
            hotelDetails.setAddress(null);
            hotelDetails.setRating(1);
            request.setHotelDetails(hotelDetails);
            ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        });
    }

    @Test
    public void deleteHotel_success() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        DeleteHotelDetailsRequest request = new DeleteHotelDetailsRequest();
        request.setId(createdHotelId);
        Object response = ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        String status = ((DeleteHotelDetailsResponse) response).getServiceStatus().getStatusCode();
        assertEquals("SUCCESS", status);
    }

    @Test
    public void deleteHotel_notFound() {
        Assertions.assertThrows(SoapFaultClientException.class, () -> {
            WebServiceTemplate ws = new WebServiceTemplate(marshaller);
            DeleteHotelDetailsRequest request = new DeleteHotelDetailsRequest();
            request.setId(0);
            Object response = ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
            String status = ((DeleteHotelDetailsResponse) response).getServiceStatus().getStatusCode();
            assertEquals("NOT_FOUND", status);
        });
    }

}
