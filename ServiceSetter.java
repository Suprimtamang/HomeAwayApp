package homeAwayApp.service;

import homeAwayApp.student.StudentGetter;

/**
 * Mutable operations for a Service.
 * Contains only methods that change the service's internal state.
 */
public interface ServiceSetter extends ServiceGetter {

    /**
     * Adds a user to the service.
     */
    void addUser(StudentGetter s);

    /**
     * Removes a user from the service.
     */
    void removeUser(StudentGetter s);

    /**
     * Updates the service rating with a new value.
     */
    void giveNewRating(int rating);

    /**
     * Adds a textual review to the service.
     */
    void addReview(String description);
}
