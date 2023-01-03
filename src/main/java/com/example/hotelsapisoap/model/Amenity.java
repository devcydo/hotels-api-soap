package com.example.hotelsapisoap.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "amenity")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "amenities", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Hotel> hotels;

    public Amenity() {
    }

    public Amenity(long id, String name, String description, List<Hotel> hotels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.hotels = hotels;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }
}
