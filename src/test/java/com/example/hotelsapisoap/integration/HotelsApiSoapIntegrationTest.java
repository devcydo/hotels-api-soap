package com.example.hotelsapisoap.integration;

import com.hotels.soap.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

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
        SaveHotelDetailsRequest request = new SaveHotelDetailsRequest();

        HotelDetails hotelDetails = new HotelDetails();
        hotelDetails.setName("Hotel from TEST 1");
        hotelDetails.setAddress("Address from Hotel TEST 1");
        hotelDetails.setRating(1);
        request.setHotelDetails(hotelDetails);

        Object response = ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);
        createdHotelId = ((SaveHotelDetailsResponse) response).getHotelDetails().getId();
        assertNotNull(response);
    }

    @Test
    public void getHotelByIdNotNull() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        GetHotelDetailsRequest request = new GetHotelDetailsRequest();
        request.setId(createdHotelId);
        assertNotNull(ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request));
    }

    @Test
    public void getHotelsNotNull() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        GetAllHotelDetailsRequest request = new GetAllHotelDetailsRequest();

        request.setPageNumber(0);
        request.setFilterByName("");
        assertNotNull(ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request));
    }

}
