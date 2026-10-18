package homeAwayApp.service;

import dataStructures.Iterator;
import homeAwayApp.student.StudentGetter;

/**
 * Interface Service provides a method to retrieve the
 * service's attributes, such as name, location, pricing, and rating info.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public interface ServiceGetter {

    /**
     * Returns a string representing the type of service.
     */
    String getType();

    /**
     * Returns the service's name.
     */
    String getName();

    /**
     * Returns the longitude of the service's location.
     */
    long getLongitude();

    /**
     * Returns the latitude of the service's location.
     */
    long getLatitude();

    /**
     * Returns the price of the service.
     * (menuPrice / roomPrice / ticketPrice, integer I/O per spec)
     */
    int getPrice();

    /**
     * Returns the service capacity.
     * (eating & lodging only; 0 or -1 if N/A)
     */
    int getCapacity();

    /**
     * Returns the student discount (leisure only; 0 if N/A).
     */
    int getDiscount();

    /**
     * @return true when usersCount() >= capacity (if capacity > 0)
     */
    boolean isFull();

    /**
     * Average rating (rounded) for ranking.
     */
    int getAvgRounded();

    /**
     * Tiebreaker for ranking (e.g. update order).
     */
    long getAvgRoundedUpdateOrder();

    /**
     * Checks if any review of this service contains the given tag (case-insensitive).
     */
    boolean hasTag(String tag);

    /**
     * Returns an iterator over the students of this service, from oldest to newest.
     */
    Iterator<StudentGetter> studentsIterator();

    /**
     * Returns a reverse iterator over the students of this service, from newest to oldest.
     */
    Iterator<StudentGetter> studentsReverseIterator();
}