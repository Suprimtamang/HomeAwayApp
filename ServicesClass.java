package homeAwayApp.collection;

import dataStructures.Iterator;
import dataStructures.DoublyLinkedList;
import dataStructures.Map;
import dataStructures.SepChainHashTable;
import homeAwayApp.service.ServiceGetter;
import homeAwayApp.service.ServiceSetter;

import java.io.Serial;
import java.io.Serializable;


/**
 * The ServicesCollectionClass represents a collection of services.
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
@SuppressWarnings("unchecked")
public class ServicesClass implements ServicesSetter, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * A list containing all services.
     */

    private DoublyLinkedList<ServiceGetter> services; // or ListInArray<Service>

    /**
     * Lists for services organized by rating (1-5 stars).
     */
    //private DoublyLinkedList<Service> [] servicesByRating; // or ListInArray<Service>[]
    private Map<Integer, DoublyLinkedList<ServiceGetter>> servicesByRating;


    /**
     * The ServicesCollectionClass constructor.
     */
    @SuppressWarnings("unchecked")
    public ServicesClass() {
        this.services = new DoublyLinkedList<>();
        this.servicesByRating = new SepChainHashTable<>(6);
        // Initialize buckets for ratings 1 to 5
        for (int stars = 1; stars <= 5; stars++) {
            getOrCreateBucket(stars);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reset() {
        this.services = new DoublyLinkedList<>();
        this.servicesByRating = new SepChainHashTable<>(6);
        // Initialize buckets for ratings 1 to 5
        for (int stars = 0; stars <= 5; stars++) {
            getOrCreateBucket(stars);
        }
    }
    // changed getSumStart to getAvgRounded
    // added check for idx range
    public void addService(ServiceGetter serviceGetter) {
        services.addLast(serviceGetter);

        int stars = serviceGetter.getAvgRounded();
        if (stars >= 1 || stars <= 5){
            getOrCreateBucket(stars).addLast(serviceGetter);
        }
    }

    @Override
    public boolean hasAnyService() {
        return !services.isEmpty();
    }

    // the length changed to serviceByrating.length. from 5
    // getSumStart changed to getAvgRounded
    @Override
    public void reorganizeCollection(ServiceGetter serviceGetter) {

        int targetStars = serviceGetter.getAvgRounded(); //- 1;

        int foundStars = -1;
        int foundPos = -1;

        // 1) Find in which rating-bucket the service is currently stored
        for (int stars = 1; stars <= 5; stars++) {
            DoublyLinkedList<ServiceGetter> bucket = servicesByRating.get(stars);
            if (bucket == null || bucket.isEmpty()) {
                continue;
            }
            int pos = bucket.indexOf(serviceGetter);
            if (pos != -1) {
                foundStars = stars;
                foundPos   = pos;
                break;
            }
        }
        // 2) If it is already in the correct bucket (rounded average didn't change),
        //    do nothing – keep the existing relative order.
        if (foundStars == targetStars) {
            return;
        }

        // 3) Remove from the old bucket (if it existed before)
        if (foundStars != -1) {
            DoublyLinkedList<ServiceGetter> oldBucket = servicesByRating.get(foundStars);
            if (oldBucket != null) {
                oldBucket.remove(foundPos);
            }
        }

        // 4) Add to the new bucket, appending at the end to preserve "older first"
        if (targetStars >= 1 && targetStars <= 5) {
            DoublyLinkedList<ServiceGetter> newBucket = servicesByRating.get(targetStars);
            if (newBucket == null) {
                newBucket = new DoublyLinkedList<>();
                servicesByRating.put(targetStars, newBucket);
            }
            newBucket.addLast(serviceGetter);
        }
    }

    public DoublyLinkedList<ServiceGetter> getInsertionList() {
        return services;
    }
    @Override
    @SuppressWarnings("unchecked")
    public DoublyLinkedList<ServiceGetter> [] getBuckets() {
        DoublyLinkedList<ServiceGetter> [] buckets = new DoublyLinkedList[5];
        for (int i = 0;i < 5; i++){
            buckets[i] = getOrCreateBucket(i + 1);
        }
       return buckets;
    }

    @Override
    public boolean isEmpty() { return services.isEmpty(); } // optional helper

    @Override
    public Iterator<ServiceGetter> getAllServices() {
        return services.iterator();
    }

    @Override
    public Iterator<ServiceGetter> getServicesGivenARating(int stars) {
        DoublyLinkedList<ServiceGetter> bucket = servicesByRating.get(stars);
        if(bucket == null){
            //return an iterator over an empty list
            return new DoublyLinkedList<ServiceGetter>().iterator();
        }
        return bucket.iterator();
    }


    @Override
    public void registerRating(ServiceSetter service, int stars, String description) {
        service.giveNewRating(stars);    // update rating first (updates lastUpdateSequence too)
        service.addReview(description);  // store the review text (for tag command)
        reorganizeCollection(service);  // move the service to the correct rating bucket
    }

    private DoublyLinkedList<ServiceGetter> getOrCreateBucket(int stars) {
        DoublyLinkedList<ServiceGetter> bucket = servicesByRating.get(stars);
        if (bucket == null) {
            bucket = new DoublyLinkedList<>();
            servicesByRating.put(stars, bucket);
        }
        return bucket;
    }

}