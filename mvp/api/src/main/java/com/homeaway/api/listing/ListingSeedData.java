package com.homeaway.api.listing;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ListingSeedData implements CommandLineRunner {
    private final ServiceListingRepository listings;

    public ListingSeedData(ServiceListingRepository listings) {
        this.listings = listings;
    }

    @Override
    public void run(String... args) {
        if (listings.count() > 0) {
            return;
        }

        listings.saveAll(List.of(
                listing("NOVA FCT", ServiceCategory.LODGING, "Monte de Caparica",
                        "A bright, friendly student residence a short walk from campus, with quiet study areas and a sunny shared terrace.",
                        420, "month", 4.9, 38, 38.6610, -9.2031,
                        "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?auto=format&fit=crop&w=960&q=85"),
                listing("Casa da Praia", ServiceCategory.LODGING, "Costa da Caparica",
                        "A relaxed shared home near the beach with a fully equipped kitchen, bicycle storage and plenty of natural light.",
                        520, "month", 4.8, 24, 38.6442, -9.2358,
                        "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=960&q=85"),
                listing("Almada Student House", ServiceCategory.LODGING, "Almada",
                        "A welcoming, furnished room with easy metro connections, high-speed Wi-Fi and a calm place to study.",
                        390, "month", 4.7, 19, 38.6793, -9.1569,
                        "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=960&q=85"),
                listing("Campus Table", ServiceCategory.EATING, "Monte de Caparica",
                        "Fresh, affordable lunches with vegetarian options, locally roasted coffee and a generous student menu.",
                        8, "meal", 4.8, 112, 38.6616, -9.2042,
                        "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=960&q=85"),
                listing("Sabor da Vila", ServiceCategory.EATING, "Costa da Caparica",
                        "A neighborhood favorite for grilled fish, seasonal sides and a laid-back dinner after a day by the sea.",
                        14, "meal", 4.7, 86, 38.6448, -9.2346,
                        "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=960&q=85"),
                listing("Café do Jardim", ServiceCategory.EATING, "Almada",
                        "Coffee, pastries and a peaceful garden patio, perfect for a study break or catching up with friends.",
                        5, "visit", 4.6, 54, 38.6799, -9.1578,
                        "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=960&q=85"),
                listing("Caparica Surf Club", ServiceCategory.LEISURE, "Costa da Caparica",
                        "Beginner-friendly surf lessons, board rental and local tips from instructors who know every break.",
                        25, "session", 4.9, 73, 38.6402, -9.2362,
                        "https://images.unsplash.com/photo-1502680390469-be75c86b636f?auto=format&fit=crop&w=960&q=85"),
                listing("Almada Culture House", ServiceCategory.LEISURE, "Almada",
                        "Discover independent exhibitions, student events and a lively calendar of music and community workshops.",
                        6, "visit", 4.5, 41, 38.6805, -9.1585,
                        "https://images.unsplash.com/photo-1577083552431-6e5fd01aa342?auto=format&fit=crop&w=960&q=85"),
                listing("Greenway Bike Hire", ServiceCategory.LEISURE, "Monte de Caparica",
                        "Pick up a comfortable city bike and explore the riverside paths, campus and Costa da Caparica coastline.",
                        12, "day", 4.6, 32, 38.6629, -9.1990,
                        "https://images.unsplash.com/photo-1485965120184-e220f721d03e?auto=format&fit=crop&w=960&q=85")
        ));
    }

    private ServiceListing listing(
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
        return new ServiceListing(name, category, neighborhood, description, price,
                priceUnit, rating, reviewCount, latitude, longitude, imageUrl);
    }
}
