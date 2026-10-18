package homeAwayApp.student;
import dataStructures.*;
import homeAwayApp.service.ServiceGetter;

/**
 * A student, with the associated name, lodging and location.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public interface StudentGetter {

    /**
     * Returns the student's name.
     * @return name
     */
    String getName();

    /**
     * Returns the student's type.
     * @return type
     */
    String getType();

    /**
     * Returns the student's home.
     *
     * @return lodging the student is living in
     */
    String getHome();

    /**
     * Returns the student's current location.
     *
     * @return location where the student is
     */
    String getLocation();

    /**
     * Returns the student's country.
     *
     * @return country where the student is from
     */
    String getCountry();

    /**
     * Checks if the student has already visited any place.
     *
     * @return <code>true</code> if the student has visited any service,
     *         <code>false</code> otherwise
     */
    boolean hasVisitedPlaces();

    /**
     * Gets the services a student has visited.
     *
     * @return the visited services iterator
     */
    Iterator<ServiceGetter> getVisited();
}