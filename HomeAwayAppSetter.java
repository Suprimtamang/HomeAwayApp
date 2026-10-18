package homeAwayApp.app;

import homeAwayApp.service.ServiceSetter;

public interface HomeAwayAppSetter extends HomeAwayAppGetter {
    /**
     * Creates a new geographical area with the specified bounds and name.
     * Resets all collections when a new area is defined.
     */
    void createArea(long latitudeTop, long longitudeLeft,
                    long latitudeBottom, long longitudeRight, String name);

    /**
     * Loads an area from a file, replacing the current in-memory state.
     *
     * @param areaName Name of the area to load
     * @return true if load was successful, false otherwise
     */
    boolean loadArea(String areaName);

    /**
     * Resets all collections and marks bounds as undefined.
     */
    void resetBounds();

    // ----- Add services -----

    void addRestaurant(long latitude, long longitude,
                       int menuPrice, int capacity, String name);

    void addLodging(long latitude, long longitude,
                    int roomPrice, int capacity, String name);

    void addLeisure(long latitude, long longitude,
                    int ticketPrice, int studentDiscount, String name);

    // ----- Student mutations -----

    void addStudent(String type, String name, String home, String lodging);

    void removeStudent(String name);

    void updateStudentLocation(String name, String location);

    void setNewHome(String name, String home);

    /**
     * Registers a rating for a specific service.
     *
     * @param service     object of the service to rate
     * @param rating      rating value from one to five stars
     * @param description review text
     */
    void registerRating(ServiceSetter service, int rating, String description);
}
