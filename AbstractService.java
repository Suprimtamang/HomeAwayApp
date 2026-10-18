package homeAwayApp.service;

import java.io.Serial;
import java.io.Serializable;
import dataStructures.DoublyLinkedList;
import dataStructures.TwoWayIterator;
import dataStructures.Iterator;
import homeAwayApp.student.StudentGetter;


/**
 * Represents any service, regardless of its type.
 * Implements the Service interface.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
abstract class AbstractService implements ServiceSetter, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // list of students in this service
    protected  DoublyLinkedList<StudentGetter> users = new DoublyLinkedList<>();
    protected DoublyLinkedList<String> reviews = new DoublyLinkedList<>();
    private final String name;
    private final long longitude;
    private final long latitude;
    protected int price;
    protected int capacity;
    protected int discount;

    // ---- Global monotonic order for tie-breaking
    private static long GLOBAL_ORDER = 0L;
    private static long nextOrder() { return ++GLOBAL_ORDER; }





    // ------- Rating -----------
    private long    sumStars;         // Total sum of starts given
    private long    ratingsCount;      // Number of times a service has been rated
    private float   avgFloat;         // Average rating as float value
    private int     avgRounded;       // Average rating rounded to nearest integer

    // ---- Tie-break clocks ----
    //private long    lastUpdateSequence;
    private long    lastAverageUpdateOrder;         // bumps on every evaluation
    private long    avgRoundedUpdateOrder;          // bumps only when avgRounded changes
    //private static long NEXT_UPDATE_SEQUENCE = 1;

    /**
     * Constructs an <code>AbstractService</code> with the given name and coordinates,
     * and sets the initial values for the ranking and times the service has been rated.
     *
     * @param name      the Service's name
     * @param longitude the Service's longitude
     * @param latitude  the Service's latitude
     */
    protected AbstractService(String name, long longitude, long latitude) {
        // -------- Initialize basic attributes -----------
        this.name       = name;
        this.longitude  = longitude;
        this.latitude   = latitude;
        this.price      = 0;
        this.capacity   = -1;
        this.discount   = 0;

        // -------- Initialize rating attributes -----------
        this.sumStars       = 4;     // default rating
        this.ratingsCount   = 1;  // includes the initial one
        this.avgFloat       = 4.0f;
        this.avgRounded     = 4;

        // -------- Initialize tie-break attributes -----------
        this.lastAverageUpdateOrder = nextOrder();
        this.avgRoundedUpdateOrder = nextOrder();

        // -------- Initialize lists -----------
        this.users      = new DoublyLinkedList<>();
        this.reviews    = new DoublyLinkedList<>();
    }

    @Override public String getName() {
        return name;
    }
    @Override public long getLongitude() {
        return longitude;
    }
    @Override public long getLatitude() {
        return latitude;
    }
    @Override public int getPrice() { return price; }
    @Override public int getCapacity() { return capacity; }
    @Override public int getDiscount() { return discount; }

    // Getters for ratings
    @Override public int  getAvgRounded(){ return avgRounded; }

    @Override public long getAvgRoundedUpdateOrder()  { return avgRoundedUpdateOrder; }


   public boolean isFull() {
        // If there's no capacity limit, it can't be full
        if (capacity < 0) return false;

        // Otherwise, check if the number of users equals or exceeds capacity
        return users.size() >= capacity;
    }
    @Override public void addUser(StudentGetter s) {
        if (s == null) return;
        // don't add if service is full
        if (isFull()) return;
        users.addLast(s);
    }

    @Override public void removeUser(StudentGetter s) {
        if (s == null) return;
        Iterator<StudentGetter> it = users.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (it.next().getName().equalsIgnoreCase(s.getName())) {
                users.remove(i);
                break;
            }
            i++;
        }
    }

    @Override
    public void giveNewRating(int newRating) {
        // Clamp to [1..5]
        if (newRating < 1) newRating = 1;
        if (newRating > 5) newRating = 5;

        //Save old rounded average before updating
        int oldRounded = this.avgRounded;

        //Update totals
        sumStars += newRating;
        ratingsCount++;

        //Compute new average (float + rounded)
        this.avgFloat = (float) sumStars / ratingsCount;
        int newRounded = Math.round(this.avgFloat);

        //Always refresh "last average update" (for general ordering)
        this.lastAverageUpdateOrder = nextOrder();

        //Only update "avgRoundedUpdateOrder" when the rounded average actually changes
        if (newRounded != oldRounded) {
            this.avgRounded = newRounded;
            this.avgRoundedUpdateOrder = nextOrder();
        }
    }


    @Override
    public void addReview(String description) {
        if (description == null || description.isEmpty()) return;
        reviews.addLast(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractService other = (AbstractService) o;
        return this.name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
    
    /**
     * The list of students currently in this service.
     */

    @Override
    public boolean hasTag(String tag) {
        if (tag == null) return false;

        String q = tag.trim().toLowerCase();
        if (q.length() == 0) return false;

        // allow only [a-z0-9] in the query token
        for (int i = 0; i < q.length(); i++) {
            char c = q.charAt(i);
            if (!isWordChar(c)) return false;
        }

        dataStructures.Iterator<String> it = reviews.iterator();
        while (it.hasNext()) {
            String rev = it.next();
            if (rev == null) continue;
            String s = rev.toLowerCase();

            int n = s.length(), m = q.length();
            for (int i = 0; i + m <= n; i++) {
                int k = 0;
                while (k < m && s.charAt(i + k) == q.charAt(k)) k++;
                if (k == m) {
                    boolean leftOk  = (i == 0) || !isWordChar(s.charAt(i - 1));
                    boolean rightOk = (i + m == n) || !isWordChar(s.charAt(i + m));
                    if (leftOk && rightOk) return true; // whole-word match
                }
            }
        }
        return false;
    }

    private static boolean isWordChar(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
    }
    @Override
    public Iterator<StudentGetter> studentsIterator() {
        return users.iterator();  // oldest to newest
    }

    @Override
    public Iterator<StudentGetter> studentsReverseIterator() {
        // Create a two-way iterator and advance it to the end, then expose a forward-only Iterator
        TwoWayIterator<StudentGetter> two = users.twoWayiterator();
        // advance to position after last element
        while (two.hasNext()) {
            two.next();
        }
        return new Iterator<StudentGetter>() {
            @Override
            public boolean hasNext() {
                return two.hasPrevious();
            }
            @Override
            public StudentGetter next() {
                return two.previous();
            }
            @Override
            public void rewind() {
                two.rewind();  // reset the two-way iterator to be positioned after the last element
                while (two.hasNext()) {
                    two.next();
                }
            }
        };
    }
}
