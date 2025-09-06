package com.springcloud.restaurant_service.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Boolean available = true;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")  // FK column in DB
    @JsonBackReference
    private Restaurant restaurant;
}
