package com.example.hotelsapisoap.unit.services;

import com.example.hotelsapisoap.model.Hotel;
import com.example.hotelsapisoap.repository.HotelRepository;
import com.example.hotelsapisoap.service.HotelServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class HotelServiceTest {
    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    static Hotel testHotel = new Hotel();

    @BeforeEach
    public void init() throws Exception {
        testHotel.setId(1L);
        testHotel.setName("Hotel from TEST 1");
        testHotel.setAddress("Address from Hotel TEST 1");
        testHotel.setRating(1);
    }

    @Test
    void createHotel() {
        doReturn(testHotel).when(hotelRepository).save(testHotel);
        Hotel resultHotel = hotelService.save(testHotel);
        assertEquals(testHotel, resultHotel);
    }
}
