package com.example.hotelsapisoap.unit.services;

import com.example.hotelsapisoap.exception.NotFoundException;
import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.model.Hotel;
import com.example.hotelsapisoap.repository.AmenityRepository;
import com.example.hotelsapisoap.repository.HotelRepository;
import com.example.hotelsapisoap.service.AmenityServiceImpl;
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
public class AmenityServiceTest {
    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private AmenityServiceImpl underTest;

    static Amenity testAmenity = new Amenity();

    @BeforeEach
    public void init() throws Exception {
        testAmenity.setId(10100L);
        testAmenity.setName("Pool");
        testAmenity.setDescription("Cool Pool");
    }

    @Test
    void createAmenity_success() {
        doReturn(testAmenity).when(amenityRepository).save(testAmenity);
        Amenity resultAmenity = underTest.addAmenity(testAmenity);
        assertEquals(testAmenity, resultAmenity);
    }

    @Test
    void editAmenity_success() {
        doReturn(Optional.of(testAmenity)).when(amenityRepository).findById(10100L);
        underTest.editAmenity(testAmenity);
        verify(amenityRepository).findById(10100L);
        verify(amenityRepository).save(testAmenity);
    }

    @Test
    void editAmenity_notFoundException() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class,()->{
            doReturn(Optional.empty()).when(amenityRepository).findById(10100L);
            underTest.editAmenity(testAmenity);
        });
    }

    @Test
    void listAmenities_success(){
        Set<Amenity> result = underTest.getAmenities();
        verify(amenityRepository).findAll();
    }

    @Test
    void deleteAmenity_success(){
        doReturn(Optional.of(testAmenity)).when(amenityRepository).findById(10100L);
        underTest.deleteById(10100L);
        verify(amenityRepository).findById(10100L);
        verify(amenityRepository).deleteById(10100L);
    }

    @Test
    void deleteAmenity_notFoundException() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class,()->{
            doReturn(Optional.empty()).when(amenityRepository).findById(10100L);
            underTest.deleteById(10100L);
        });
    }
}
