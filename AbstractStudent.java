package homeAwayApp.student;

import dataStructures.DoublyLinkedList;
import dataStructures.Iterator;
import homeAwayApp.service.ServiceGetter;

import java.io.Serial;
import java.io.Serializable;

/**
 * AbstractStudent implements the Student interface and represents a Student.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */

abstract class AbstractStudent implements StudentSetter, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * The student's <code>name</code>.
     */
    private final String name;



    /**
     * The student's <code>location</code>.
     */
    private String location;

    /**
     * The student's <code>home</code>.
     */
    private String home;


    private final String country;
    /**
     * The services the student has visited.
     */
    private final DoublyLinkedList<ServiceGetter> visited;

    /**
     * Constructs a new AbstractStudent with the given name and home location.
     *
     * @param name    Student's name
     * @param country Student's country
     * @param lodging Current lodging of the student
     */
    protected AbstractStudent(String name, String country, String lodging) {
        this.name = name;
        this.country = country;
        this.home = lodging;
        this.location = lodging;
        this.visited = new DoublyLinkedList<>();
    }

    @Override
    public String getName() {
        return name;
    }


    public String getCountry() {
        return country;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getHome() {
        return home;
    }

    @Override
    public void setNewHome(String newHome) {
        home = newHome;
    }

    @Override
    public void setLocation(String newLocation) {
        location = newLocation;
    }

    @Override
    public void addVisitedLocation(ServiceGetter serviceGetter) {
        if (visited.indexOf(serviceGetter) == -1) {
            visited.addLast(serviceGetter);
        }
    }

    @Override
    public Iterator<ServiceGetter> getVisited() {
        return visited.iterator();
    }

    @Override
    public boolean hasVisitedPlaces() {
        return !visited.isEmpty();
    }

}
