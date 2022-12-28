package com.example.localhost.model;

import javax.persistence.*;
import java.util.Set;

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

    @ManyToMany(mappedBy = "amenities", fetch = FetchType.EAGER)
    private Set<Hotel> hotels;

    public Amenity() {
    }

    public Amenity(long id, String name, String description, Set<Hotel> hotels) {
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

    public Set<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(Set<Hotel> hotel) {
        this.hotels = hotels;
    }
}
