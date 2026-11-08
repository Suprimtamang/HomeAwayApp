package com.homeaway.api.listing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "service_listings")
public class ServiceListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;

    @NotBlank
    @Column(nullable = false)
    private String neighborhood;

    @NotBlank
    @Column(nullable = false, length = 1200)
    private String description;

    @Positive
    @Column(nullable = false)
    private int price;

    @NotBlank
    @Column(nullable = false)
    private String priceUnit;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    @Column(nullable = false)
    private double rating;

    @Min(0)
    @Column(nullable = false)
    private int reviewCount;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Column(nullable = false)
    private double latitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(nullable = false)
    private double longitude;

    @NotBlank
    @Column(nullable = false)
    private String imageUrl;

    protected ServiceListing() {
    }

    public ServiceListing(
            String name,
            ServiceCategory category,
            String neighborhood,
            String description,
            int price,
            String priceUnit,
            double rating,
            int reviewCount,
            double latitude,
            double longitude,
            String imageUrl
    ) {
        this.name = name;
        this.category = category;
        this.neighborhood = neighborhood;
        this.description = description;
        this.price = price;
        this.priceUnit = priceUnit;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
