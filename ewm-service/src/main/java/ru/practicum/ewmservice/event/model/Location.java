package ru.practicum.ewmservice.event.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Setter
@Getter
public class Location {
    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat")
    private double lat; //Широта

    @Column(name = "lon", unique = true)
    private double lon; //Долгота
}