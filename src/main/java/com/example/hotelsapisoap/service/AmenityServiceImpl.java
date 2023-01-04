package com.example.hotelsapisoap.service;

import com.example.hotelsapisoap.exception.BadRequestException;
import com.example.hotelsapisoap.exception.NotFoundException;
import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.model.Hotel;
import com.example.hotelsapisoap.repository.AmenityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository repository;

    public AmenityServiceImpl(AmenityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Amenity getById(long id){
        Optional<Amenity> optionalAmenity = repository.findById(id);

        if(!optionalAmenity.isPresent())
            throw new NotFoundException("Amenity with id: " + id + " not found");

        return optionalAmenity.get();
    };

    @Override
    public Set<Amenity> getAmenities() { return repository.findAll(); }

    @Override
    public Set<Amenity> getByHotel(long id_hotel) {
        Set<Amenity> amenities = repository.findAmenitiesByHotelsId(id_hotel);
        if(amenities == null)
            throw new NotFoundException("Amenities for hotel id: " + id_hotel + " not found");
        return amenities;
    }

    @Override
    public Amenity addAmenity(Amenity amenity) {
        Optional<Amenity> optionalAmenity = repository.findById(amenity.getId());
        if(optionalAmenity.isPresent())
            throw new BadRequestException("Amenity with id: " + amenity.getId() + " already exists");
        return repository.save(amenity);
    }

    @Override
    public Amenity editAmenity(Amenity amenity) throws NotFoundException {
        Optional<Amenity> optionalAmenity = repository.findById(amenity.getId());

        if(!optionalAmenity.isPresent())
            throw new NotFoundException("Amenity with id: " + amenity.getId() + " not found");

        Amenity amenityToSave = optionalAmenity.get();
        amenityToSave.setName(amenity.getName());
        amenityToSave.setDescription(amenity.getDescription());
        return repository.save(amenityToSave);

    }

    public void deleteById(long id) {
        Optional<Amenity> optionalAmenity = repository.findById(id);
        if(!optionalAmenity.isPresent())
            throw new NotFoundException("Amenity with id: " + id + " not found");
        repository.deleteById(id);
    }

}
