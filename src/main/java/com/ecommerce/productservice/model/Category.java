package com.ecommerce.productservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;


import java.util.UUID;

@Entity
@Table(name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "codeCategory")})
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  long id;

    private String nameCategory;

    private String description;

    @Column(unique = true)
    private String codeCategory;


    @ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();
}



