package com.example.localhost.service;

import com.example.localhost.exception.ServiceFaultException;
import com.example.localhost.model.Hotel;
import com.example.localhost.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class HotelService {

    private HotelRepository repository;

    public HotelService(){}

    @Autowired
    public HotelService(HotelRepository repository){
        this.repository = repository;
    }

    public Hotel getById(long id){
        Hotel hotel = repository.findById(id);
        return hotel;
    };

    public Set<Hotel> getAll() {
        return repository.findAll();
    };

    public Hotel save(Hotel hotel) {
        try {
            return repository.save(hotel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteById(long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
