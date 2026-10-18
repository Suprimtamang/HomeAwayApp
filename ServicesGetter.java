package homeAwayApp.collection;

import dataStructures.DoublyLinkedList;
import dataStructures.Iterator;
import homeAwayApp.service.ServiceGetter;

/**
 * Interface representing a collection of services.
 * Provides methods for adding, retrieving, and organizing services,
 * as well as filtering them based on their star ratings.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public interface ServicesGetter {

    /**
     * Verifies whether the collection contains any services.
     *
     * @return <code>true</code> if there is at least one service,
     * <code>false</code> otherwise.
     */
    boolean hasAnyService();

    /**
     * Retrieves an iterator over all services in the collection.
     *
     * @return Iterator of all services.
     */
    Iterator<ServiceGetter> getAllServices();

    /**
     * Returns an iterator over a collection of services,
     * with a current rank of a given amount of <code>stars</code>.
     *
     * @param stars the ranking of the services
     * @return iterator of services
     */
    Iterator<ServiceGetter> getServicesGivenARating(int stars);

    /**
     * Gets the internal buckets used to organize services by rating.
     */
    DoublyLinkedList<ServiceGetter>[] getBuckets();

    /**
     * Checks if the collection has no services.
     *
     * @return <code>true</code> if empty, <code>false</code> otherwise
     */
    boolean isEmpty();

    /**
     * Gets the list that preserves insertion order of services.
     *
     * @return doubly linked list of services in insertion order
     */
    DoublyLinkedList<ServiceGetter> getInsertionList();
}