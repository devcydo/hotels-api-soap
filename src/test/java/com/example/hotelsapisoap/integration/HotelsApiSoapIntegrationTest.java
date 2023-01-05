package com.example.hotelsapisoap.integration;

import com.hotels.soap.*;
import org.junit.jupiter.api.*;
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
        AddHotelDetailsRequest addHotelDetailsRequest = new AddHotelDetailsRequest();

        HotelDetails hotelDetails = new HotelDetails();
        hotelDetails.setName("Hotel from TEST integration");
        hotelDetails.setAddress("Address from Hotel TEST integration");
        hotelDetails.setRating(5);
        addHotelDetailsRequest.setHotelDetails(hotelDetails);

        Object hotelResponse = ws.marshalSendAndReceive("http://localhost:" + port + "/ws", addHotelDetailsRequest);
        createdHotelId = ((AddHotelDetailsResponse) hotelResponse).getHotelDetails().getId();
        assertNotNull(hotelResponse);

        AddAmenityDetailsRequest addAmenityDetailsRequest = new AddAmenityDetailsRequest();

        AmenityDetails amenityDetails = new AmenityDetails();
        amenityDetails.setName("Outdoor pool");
        amenityDetails.setDescription("Open from 8:00 to 20:00");
        addAmenityDetailsRequest.setAmenityDetails(amenityDetails);

        Object amenityResponse = ws.marshalSendAndReceive("http://localhost:" + port + "/ws", addAmenityDetailsRequest);
        createdAmenityId = ((AddAmenityDetailsResponse) amenityResponse).getAmenityDetails().getId();
        assertNotNull(amenityResponse);
    }

    @AfterEach
    public void terminate(TestInfo info) {

        if(info.getDisplayName().equals("deleteHotel_success()") || info.getDisplayName().equals("addAmenityToHotel_success()")) return;

        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        DeleteAmenityDetailsRequest deleteAmenityDetailsRequest = new DeleteAmenityDetailsRequest();
        deleteAmenityDetailsRequest.setId(createdAmenityId);
        ws.marshalSendAndReceive("http://localhost:" + port + "/ws", deleteAmenityDetailsRequest);

        DeleteHotelDetailsRequest deleteHotelDetailsRequest = new DeleteHotelDetailsRequest();
        deleteHotelDetailsRequest.setId(createdHotelId);
        ws.marshalSendAndReceive("http://localhost:" + port + "/ws", deleteHotelDetailsRequest);
    }

    @Test
    public void addHotel_badRequest(){
        Assertions.assertThrows(SoapFaultClientException.class, () -> {
           WebServiceTemplate ws = new WebServiceTemplate(marshaller);
           AddHotelDetailsRequest request = new AddHotelDetailsRequest();
           HotelDetails hotelDetails = new HotelDetails();
           hotelDetails.setName("");
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

    @Test
    public void addAmenityToHotel_success() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        AddAmenityDetailsToHotelDetailsRequest request = new AddAmenityDetailsToHotelDetailsRequest();
        request.setIdHotel(createdHotelId);
        request.setIdAmenity(createdAmenityId);
        Object response = ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        HotelDetails hotelResponse = ((AddAmenityDetailsToHotelDetailsResponse) response).getHotelDetails();
        assertEquals(createdHotelId, hotelResponse.getId());
    }

    @Test
    public void addAmenityToHotel_notFound() {
        Assertions.assertThrows(SoapFaultClientException.class, () -> {
            WebServiceTemplate ws = new WebServiceTemplate(marshaller);
            AddAmenityDetailsToHotelDetailsRequest request = new AddAmenityDetailsToHotelDetailsRequest();
            request.setIdHotel(0);
            request.setIdAmenity(0);
            ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        });
    }

    @Test
    public void addAmenity_badRequest(){
        Assertions.assertThrows(SoapFaultClientException.class, () -> {
            WebServiceTemplate ws = new WebServiceTemplate(marshaller);
            AddAmenityDetailsRequest request = new AddAmenityDetailsRequest();
            AmenityDetails amenityDetails = new AmenityDetails();
            amenityDetails.setName("WiFi");
            amenityDetails.setDescription("");
            request.setAmenityDetails(amenityDetails);
            ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        });
    }

    @Test
    public void getAmenities_success() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        GetAllAmenityDetailsRequest request = new GetAllAmenityDetailsRequest();
        assertNotNull(ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request));
    }

    @Test
    public void getAmenitiesByHotelId_success() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        GetAllAmenityDetailsByHotelRequest request = new GetAllAmenityDetailsByHotelRequest();
        request.setIdHotel(createdHotelId);
        assertNotNull(ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request));
    }

    @Test
    public void editAmenity_success() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        EditAmenityDetailsRequest request = new EditAmenityDetailsRequest();
        AmenityDetails amenityDetails = new AmenityDetails();
        amenityDetails.setId(createdAmenityId);
        amenityDetails.setName("Pool");
        amenityDetails.setDescription("Very cool pool");
        request.setAmenityDetails(amenityDetails);

        Object response = ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        AmenityDetails amenityResponse = ((EditAmenityDetailsResponse) response).getAmenityDetails();
        assertEquals(amenityDetails.getId(), amenityResponse.getId());
        assertEquals(amenityDetails.getName(), amenityResponse.getName());
        assertEquals(amenityDetails.getDescription(), amenityResponse.getDescription());
    }
    @Test
    public void editAmenity_badRequest(){
        Assertions.assertThrows(SoapFaultClientException.class, () -> {
            WebServiceTemplate ws = new WebServiceTemplate(marshaller);
            EditAmenityDetailsRequest request = new EditAmenityDetailsRequest();
            AmenityDetails amenityDetails = new AmenityDetails();
            amenityDetails.setId(createdHotelId);
            amenityDetails.setName(null);
            amenityDetails.setDescription(null);
            request.setAmenityDetails(amenityDetails);
            ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        });
    }
}
