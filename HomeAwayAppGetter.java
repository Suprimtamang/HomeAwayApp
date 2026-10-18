package homeAwayApp.app;

import dataStructures.Iterator;
import homeAwayApp.service.ServiceGetter;
import homeAwayApp.student.StudentGetter;

/**
 * Represents the app, with a collection of students, services and their attributes.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public interface HomeAwayAppGetter {
    // ----- Bounds & Area info -----

    boolean areValidBounds(long latitudeTop, long longitudeLeft,
                           long latitudeBottom, long longitudeRight);

    boolean areBoundsDefined();

    /**
     * Saves the current area to a file.
     *
     * @return true if save was successful, false otherwise
     */
    boolean saveArea();

    /**
     * Checks if an area with the given name exists as a saved file.
     *
     * @param areaName Name of the area to check
     * @return true if area exists, false otherwise
     */
    boolean areaExists(String areaName);

    boolean isWithinBounds(long latitude, long longitude);

    /**
     * Gets the name of the current area.
     *
     * @return Name of the area
     */
    String getAreaName();

    // ----- Validation helpers -----

    boolean isInValidPrice(double price);

    boolean checkDiscount(int discount);

    boolean checkValue(int value);

    boolean isRatingInvalid(int stars);

    boolean validServiceType(String serviceType);

    boolean validStudentType(String studentType);

    // ----- Existence & type checks -----

    boolean hasStudent(String name);

    boolean hasService(String name);

    boolean hasAnyStudents();

    boolean hasAnyServices();

    boolean isStudentThrifty(String name);

    boolean hasServiceWithStars(String serviceType, int star);

    boolean hasVisitedLocations(String name);

    boolean hasServicesOfType(String type);

    boolean isMovingOK(String name, String lodging);

    boolean moreExpensive(String name, String location);

    boolean isStudentAtHome(String name);

    boolean isLodgingFullByResidents(ServiceGetter lodging);

    // ----- Getters & iterators -----

    ServiceGetter getStudentLocation(String name);

    String getStudentLodging(String name);

    Iterator<ServiceGetter> getAllServices();

    Iterator<StudentGetter> getAllStudents();

    Iterator<ServiceGetter> getVisitedPlaces(String name);

    String getRecommendedService(String name, String type);

    ServiceGetter getServiceByName(String name);

    Iterator<StudentGetter> getStudentsByCountry(String country);

    StudentGetter getStudentByName(String name);

    // ----- Listing / printing methods -----

    void listUsers(String order, String serviceName);

    void listServicesByRanking();

    void listRankedByTypeAndAverage(String type, int rank, String studentName);

    void listServicesWithTag(String word);

    }


