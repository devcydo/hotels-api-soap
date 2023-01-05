package com.example.hotelsapisoap.service;

import com.example.hotelsapisoap.exception.BadRequestException;
import com.example.hotelsapisoap.exception.NotFoundException;
import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.model.Hotel;
import com.example.hotelsapisoap.repository.AmenityRepository;
import com.example.hotelsapisoap.repository.HotelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.hotelsapisoap.helper.HotelHelper.validateHotel;

@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository repository;
    private final AmenityRepository amenityRepository;

    public HotelServiceImpl(HotelRepository repository, AmenityRepository amenityRepository){
        this.repository = repository;
        this.amenityRepository = amenityRepository;
    }

    @Override
    public Hotel getById(long id){
        Optional<Hotel> hotel = repository.findById(id);

        if(!hotel.isPresent())
            throw new NotFoundException("Hotel with id: " + id + " not found.");

        return hotel.get();
    };

    @Override
    public Page<Hotel> getHotels(int page, String name) {
        Pageable pageable = PageRequest.of(page, 5);

        if(!name.equals("")) {
            return repository.findHotelsByNameContainingIgnoreCase(name, pageable);
        }

        return repository.findAll(pageable);
    }

    @Override
    public Hotel addHotel(Hotel hotel) throws BadRequestException, BadRequestException {
        Optional<Hotel> optionalHotel = repository.findById(hotel.getId());
        if(optionalHotel.isPresent())
            throw new BadRequestException("Hotel with id: " + hotel.getId() + " already exists");
        validateHotel(hotel);

        return repository.save(hotel);
    }

    @Override
    public Hotel editHotel(Hotel hotel) throws NotFoundException, BadRequestException {
        Optional<Hotel> optionalHotel = repository.findById(hotel.getId());
        if(!optionalHotel.isPresent())
            throw new NotFoundException("Hotel with id: " + hotel.getId() + " not found");
        validateHotel(hotel);

        Hotel hotelToSave = optionalHotel.get();
        hotelToSave.setName(hotel.getName());
        hotelToSave.setAddress(hotel.getAddress());
        hotelToSave.setRating(hotel.getRating());
        return repository.save(hotelToSave);
    }

    @Override
    public Hotel addAmenity(long id_hotel, long id_amenity) {
        Optional<Hotel> optionalHotel = repository.findById(id_hotel);

        if(!optionalHotel.isPresent())
            throw new NotFoundException("Hotel with id: " + id_hotel + " not found");

        Optional<Amenity> optionalAmenity = amenityRepository.findById(id_amenity);

        if(!optionalAmenity.isPresent())
            throw new NotFoundException("Amenity with id: " + id_amenity + " not found");

        Hotel hotel = optionalHotel.get();
        Amenity amenity = optionalAmenity.get();

        hotel.addAmenity(amenity);

        return repository.save(hotel);
    }

    public Hotel removeAmenity(long id_hotel, long id_amenity) {
        Optional<Hotel> optionalHotel = repository.findById(id_hotel);

        if(!optionalHotel.isPresent())
            throw new NotFoundException("Hotel with id: " + id_hotel + " not found");

        Optional<Amenity> optionalAmenity = amenityRepository.findById(id_amenity);

        if(!optionalAmenity.isPresent())
            throw new NotFoundException("Amenity with id: " + id_amenity + " not found");

        Hotel hotel = optionalHotel.get();

        hotel.removeAmenity(id_amenity);
        return repository.save(hotel);
    }

    public void deleteById(long id) {
        Optional<Hotel> optionalHotel = repository.findById(id);

        if(!optionalHotel.isPresent())
            throw new NotFoundException("Hotel with id: " + id + " not found");

        repository.deleteById(id);
    }
}
