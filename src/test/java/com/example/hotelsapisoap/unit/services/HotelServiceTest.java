package com.example.hotelsapisoap.unit.services;

import com.example.hotelsapisoap.exception.NotFoundException;
import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.model.Hotel;
import com.example.hotelsapisoap.repository.AmenityRepository;
import com.example.hotelsapisoap.repository.HotelRepository;
import com.example.hotelsapisoap.service.HotelServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class HotelServiceTest {
    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl underTest;

    static Hotel testHotel = new Hotel();
    static Amenity testAmenity = new Amenity();

    @BeforeEach
    public void init() throws Exception {
        testHotel.setId(10010L);
        testHotel.setName("Hotel from TEST 1");
        testHotel.setAddress("Address from Hotel TEST 1");
        testHotel.setRating(1);

        testAmenity.setId(10L);
        testAmenity.setName("Pool");
        testAmenity.setDescription("Cool Pool");
    }

    @Test
    void createHotel_success() {
        doReturn(testHotel).when(hotelRepository).save(testHotel);
        Hotel resultHotel = underTest.addHotel(testHotel);
        assertEquals(testHotel, resultHotel);
    }

    @Test
    void editHotel_success() {
        doReturn(Optional.of(testHotel)).when(hotelRepository).findById(10010L);
        underTest.editHotel(testHotel);
        verify(hotelRepository).findById(10010L);
        verify(hotelRepository).save(testHotel);
    }

    @Test
    void editHotel_notFoundException() throws NotFoundException{
        Assertions.assertThrows(NotFoundException.class,()->{
            doReturn(Optional.empty()).when(hotelRepository).findById(10010L);
            underTest.editHotel(testHotel);
        });
    }

    @Test
    void listHotelsWithPagination_success(){
        Page<Hotel> result = underTest.getHotels(0,"");
        verify(hotelRepository).findAll(PageRequest.of(0,5));
    }

    @Test
    void filterHotelsByName(){
        Page<Hotel> result = underTest.getHotels(0,"test");
        verify(hotelRepository).findHotelsByNameContainingIgnoreCase("test",PageRequest.of(0,5));
    }

    @Test
    void deleteHotel_success(){
        doReturn(Optional.of(testHotel)).when(hotelRepository).findById(10010L);
        underTest.deleteById(10010L);
        verify(hotelRepository).findById(10010L);
        verify(hotelRepository).deleteById(10010L);
    }

    @Test
    void deleteHotel_notFoundException() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class,()->{
            doReturn(Optional.empty()).when(hotelRepository).findById(10010L);
            underTest.deleteById(10010L);
        });
    }

    @Test
    void addAmenityToHotel_notFoundException() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class,()->{
            doReturn(Optional.empty()).when(hotelRepository).findById(10010L);
            underTest.addAmenity(10010L,10L);
        });
    }
}
