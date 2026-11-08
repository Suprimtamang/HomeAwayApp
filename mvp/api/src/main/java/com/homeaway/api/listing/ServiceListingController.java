package com.homeaway.api.listing;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class ServiceListingController {
    private final ServiceListingRepository listings;

    public ServiceListingController(ServiceListingRepository listings) {
        this.listings = listings;
    }

    @GetMapping
    public List<ServiceListing> findListings(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) ServiceCategory category,
            @RequestParam(required = false) Integer maxPrice
    ) {
        Stream<ServiceListing> results = listings.findAll().stream();
        if (q != null && !q.isBlank()) {
            String query = q.strip().toLowerCase(Locale.ROOT);
            results = results.filter(listing ->
                    listing.getName().toLowerCase(Locale.ROOT).contains(query)
                            || listing.getNeighborhood().toLowerCase(Locale.ROOT).contains(query)
                            || listing.getDescription().toLowerCase(Locale.ROOT).contains(query));
        }
        if (category != null) {
            results = results.filter(listing -> listing.getCategory() == category);
        }
        if (maxPrice != null) {
            results = results.filter(listing -> listing.getPrice() <= maxPrice);
        }
        return results.toList();
    }

    @GetMapping("/{id}")
    public ServiceListing findListing(@PathVariable Long id) {
        return getListing(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceListing createListing(@Valid @RequestBody ListingRequest request) {
        ServiceListing listing = new ServiceListing(
                request.name().strip(),
                request.category(),
                request.neighborhood().strip(),
                request.description().strip(),
                request.price(),
                request.priceUnit().strip(),
                request.rating(),
                request.reviewCount(),
                request.latitude(),
                request.longitude(),
                request.imageUrl().strip()
        );
        return listings.save(listing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteListing(@PathVariable Long id) {
        listings.delete(getListing(id));
    }

    private ServiceListing getListing(Long id) {
        Optional<ServiceListing> listing = listings.findById(id);
        return listing.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found"));
    }

    public record ListingRequest(
            @jakarta.validation.constraints.NotBlank String name,
            @jakarta.validation.constraints.NotNull ServiceCategory category,
            @jakarta.validation.constraints.NotBlank String neighborhood,
            @jakarta.validation.constraints.NotBlank String description,
            @Min(1) int price,
            @jakarta.validation.constraints.NotBlank String priceUnit,
            @jakarta.validation.constraints.DecimalMin("0.0")
            @jakarta.validation.constraints.DecimalMax("5.0") double rating,
            @jakarta.validation.constraints.Min(0) int reviewCount,
            @jakarta.validation.constraints.DecimalMin("-90.0")
            @jakarta.validation.constraints.DecimalMax("90.0") double latitude,
            @jakarta.validation.constraints.DecimalMin("-180.0")
            @jakarta.validation.constraints.DecimalMax("180.0") double longitude,
            @jakarta.validation.constraints.NotBlank String imageUrl
    ) {
    }
}
