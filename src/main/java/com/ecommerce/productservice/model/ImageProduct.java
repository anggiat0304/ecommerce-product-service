package com.ecommerce.productservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "image_product",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")})
@Getter
@Setter
@Data
public class ImageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  long id;

    @Lob // Menggunakan anotasi @Lob untuk menyesuaikan dengan jenis kolom yang mendukung BLOB di database
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private String imageData;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;
}
