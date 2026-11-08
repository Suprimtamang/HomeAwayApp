package com.homeaway.api.listing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceListingRepository extends JpaRepository<ServiceListing, Long> {
}
