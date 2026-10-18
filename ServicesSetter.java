package homeAwayApp.collection;

import homeAwayApp.service.ServiceGetter;
import homeAwayApp.service.ServiceSetter;

public interface ServicesSetter extends ServicesGetter {

    /**
     * Removes all services from the collection, resetting it to an empty state.
     */
    void reset();

    /**
     * Adds a new service to the collection.
     *
     * @param serviceGetter The service to add to the collection
     */
    void addService(ServiceGetter serviceGetter);

    /**
     * Updates the position of a service in the collection,
     * after its rating has changed.
     *
     * @param serviceGetter The service to reorganize.
     */
    void reorganizeCollection(ServiceGetter serviceGetter);

    /**
     * Registers a new rating and review for a service,
     * updating its rating information and reorganizing the collection.
     *
     * @param service     the service to rate
     * @param stars       rating value
     * @param description review text
     */
    void registerRating(ServiceSetter service, int stars, String description);
}