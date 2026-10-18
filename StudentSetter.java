package homeAwayApp.student;

import homeAwayApp.service.ServiceGetter;

/**
 * Mutable operations for a Student.
 * Contains only methods that change the student's internal state.
 */
public interface StudentSetter extends StudentGetter {

    /**
     * Changes the student's home to another lodging service.
     *
     * @param home the new lodging service the student will move to
     */
    void setNewHome(String home);

    /**
     * Changes the service a student is in, to {@code location}.
     *
     * @param location the new location the student is in
     */
    void setLocation(String location);

    /**
     * Adds a service to the array of locations the student has visited.
     *
     * @param location the service to be added
     */
    void addVisitedLocation(ServiceGetter location);
}
