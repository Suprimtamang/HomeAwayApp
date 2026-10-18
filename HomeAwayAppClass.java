package homeAwayApp.app;

import dataStructures.*;
import homeAwayApp.area.BoundsClass;
import homeAwayApp.area.BoundsSetter;
import homeAwayApp.collection.*;
import homeAwayApp.collection.StudentsSetter;
import homeAwayApp.constants.*;
import homeAwayApp.service.*;
import homeAwayApp.student.*;

import java.io.*;

/**
 * Implements the HomeAwayApp interface.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public class HomeAwayAppClass implements HomeAwayAppSetter, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private BoundsSetter bounds;
    private ServicesSetter services;
    private StudentsSetter students;
    private String areaName;
    //HashMaps
    private Map<String, StudentGetter> studentsMap;
    private Map<String, ServiceGetter> servicesMap;
    private Map<String, Integer> serviceCountByType;

    /**
     * Constructor for the HomeAwayAppClass.
     */
    public HomeAwayAppClass() {
        this.bounds = new BoundsClass();
        this.services = new ServicesClass();
        this.students = new StudentsClass();
        //HashMaps
        this.studentsMap = new SepChainHashTable<>();
        this.servicesMap = new SepChainHashTable<>();
        //New
        this.serviceCountByType = new SepChainHashTable<>();
    }

    @Override
    public boolean areValidBounds(long latitudeTop, long longitudeTop, long latitudeBottom, long longitudeRight) {
        return bounds.areValidBounds(latitudeTop, longitudeTop, latitudeBottom, longitudeRight);
    }

    @Override
    public boolean areBoundsDefined() {
        return bounds.areBoundsDefined();
    }

    @Override
    public void createArea(long latitudeTop, long longitudeLeft, long latitudeBottom, long longitudeRight, String name) {
        resetBounds();
        bounds.createArea(latitudeTop, longitudeLeft, latitudeBottom, longitudeRight, name);
    }

    @Override
    public boolean saveArea() {
        String fileName = getAreaName().replace(' ','_') + ".ser";     //Save to root directory with .ser extension
        File file = new File(fileName);

        // Try serializing the current area
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this); // Serialize the entire current object
            oos.flush();  // delete and test if work fine delete this line
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    @Override
    public boolean loadArea(String areaName) {
        String fileName = areaName.replace(' ', '_') + ".ser";
        File file = new File(fileName);

        // 1. If the exact file exists, just load it
        if (file.exists()) {
            return loadFromFile(file);
        }

        // 2. Otherwise, try to find a file ignoring case (using lambda)
        File currentDir = new File(".");  // current working directory
        File[] serFiles = currentDir.listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".ser")
        );

        if (serFiles == null)
            return false;
        File matchedFile = null;
        String matchedName = null;
        // 3. Find the correct .ser file ignoring case
        for (File f : serFiles) {
            String base = f.getName().substring(0, f.getName().length() - 4); // remove ".ser"
            base = base.replace('_', ' '); // restore original spaces

            if (base.equalsIgnoreCase(areaName)) {
                matchedFile = f;
                matchedName = base; // preserve the original capitalization
                break;
            }
        }

        // 4. If nothing found, return false
        if (matchedFile == null)
            return false;

        // 5. Otherwise, load and update current state
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(matchedFile))) {
            HomeAwayAppClass loaded = (HomeAwayAppClass) ois.readObject();
            this.bounds   = loaded.bounds;
            this.services = loaded.services;
            this.students = loaded.students;
            this.areaName = loaded.areaName != null ? loaded.areaName : matchedName;
            // rebuild indexes with new loaded data from service and student collections
            rebuildIndexes();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    private boolean loadFromFile(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            HomeAwayAppClass loaded = (HomeAwayAppClass) ois.readObject();
            this.bounds = loaded.bounds;
            this.services = loaded.services;
            this.students = loaded.students;
            this.areaName = loaded.areaName;
            // rebuild indexes with new loaded data from service and student collections
            rebuildIndexes();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }

    }

    @Override
    public boolean areaExists(String areaName) {
        if (areaName == null) return false;
        String target = areaName.trim();

        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".ser"));
        if (files == null) return false;

        for (File f : files) {
            String name = f.getName();
            String base = name.substring(0, name.length() - 4) // drop ".ser"
                    .replace('_', ' ');
            if (base.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void resetBounds() {
        services.reset();
        students.reset();
        bounds.resetBoundsName();

        // rebuild empty HashMaps indexes
        this.studentsMap = new  SepChainHashTable<>();
        this.servicesMap = new  SepChainHashTable<>();
        // New
        this.serviceCountByType = new SepChainHashTable<>();
    }

    @Override
    public boolean isWithinBounds(long latitude, long longitude) {
        return bounds.isWithinBounds(latitude, longitude);
    }

    @Override
    public boolean isInValidPrice(double price) {
        return price <= 0;
    }

    @Override
    public boolean checkDiscount(int discount) {
        return discount < 0 || discount > 100;
    }

    public boolean checkValue(int value) {
        if (value <= 0) {
            return true;
        }
        return false;
    }

    // the Capacity for Eating and Lodging and Discount for Leisure value is unknown here.
    @Override
    public void addRestaurant(long latitude, long longitude, int menuPrice,int capacity ,String name ) {
        addService(ServiceType.EATING,latitude, longitude, menuPrice, capacity, name);
    }

    @Override
    public String getAreaName() {
        return bounds.getName(); // this needs to be implemented if need be
    }

    @Override
    public void addLodging(long latitude, long longitude, int roomPrice, int capacity ,String name) {
        addService(ServiceType.LODGING,latitude, longitude, roomPrice, capacity, name);
    }

    @Override
    public void addLeisure(long latitude, long longitude, int ticketPrice, int studentDiscount, String name) {
        addService(ServiceType.LEISURE,latitude, longitude, ticketPrice, studentDiscount, name);
    }

    private void addService(ServiceType type, long latitude, long longitude, int price, int value, String name){
        ServiceGetter serviceGetter = type.create(latitude, longitude, price, value, name);
        services.addService(serviceGetter);
        String key = keyof(serviceGetter.getName());
        if (key != null) {
            servicesMap.put(key, serviceGetter);
        }
        // NEW: update count by type
        String typeKey = serviceGetter.getType(); // e.g., "EATING", "LODGING", "LEISURE"
        Integer count = serviceCountByType.get(typeKey);
        if (count == null) {
            serviceCountByType.put(typeKey, 1);
        } else {
            serviceCountByType.put(typeKey, count + 1);
        }
    }

    @Override
    public boolean hasService(String name) {
        return searchForService(name) != null;
    }

    @Override
    public void addStudent(String type, String name, String country, String lodging) {
        StudentType studentType = StudentType.fromString(type);
        if (studentType == null) return;
        homeAwayApp.student.StudentSetter student = (homeAwayApp.student.StudentSetter) studentType.create(name, country, lodging);
        // Add to global students collection
        students.addStudent(student);
        // index by name
        String key = keyof(student.getName());
        if (key != null) {
            studentsMap.put(key, student);
        }
        // Find home lodging once
        // Canonicalize home and location and sync users list
        ServiceSetter lodgingService = (ServiceSetter) searchForService(lodging);
        if (lodgingService != null) {
            String canonical = lodgingService.getName();           // canonical name
            student.setNewHome(canonical);                         // just updates the field
            student.setLocation(canonical);
            lodgingService.addUser(student);                       // keep users in sync
            if (student.getType().equalsIgnoreCase(StudentType.OUTGOING.toString())) {
                student.addVisitedLocation(lodgingService);
            }
        }
    }

    @Override
    public void removeStudent(String name) {
        StudentGetter studentGetterToRemove = searchForStudent(name);
        ServiceSetter where = (ServiceSetter) searchForService(studentGetterToRemove.getLocation());
        if (where != null) {
            where.removeUser(studentGetterToRemove); // keep “users” in sync
        }
        students.removeStudent(studentGetterToRemove);
        // remove from index
        String key = keyof(studentGetterToRemove.getName());
        if (key != null) {
            studentsMap.remove(key);
        }
    }


    @Override
    public boolean hasStudent(String name) {
        return searchForStudent(name) != null;
    }

    @Override
    public boolean isStudentAtHome(String name) {
        StudentGetter studentGetter = searchForStudent(name);
        return studentGetter.getHome().equals(studentGetter.getLocation());
    }

    @Override
    public void updateStudentLocation(String name, String location) {
        homeAwayApp.student.StudentSetter student = (homeAwayApp.student.StudentSetter) searchForStudent(name);
        ServiceSetter current = (ServiceSetter) searchForService(student.getLocation());
        boolean goingHome = location.equalsIgnoreCase(HomeType.HOME.toString());
        ServiceSetter target = (ServiceSetter) (goingHome ? searchForService(student.getHome()) : searchForService(location));

        boolean alreadyThere = (current != null && current.getName().equalsIgnoreCase(target.getName()));
        if (!alreadyThere && target.getCapacity() > 0 && target.isFull()) {
            return; // Cannot move, capacity exceeded
        }

        // Thrifty rule: allow auto‑home update only if target is not full
        if (!goingHome
                && student.getType().equalsIgnoreCase(StudentType.THRIFTY.toString())
                && target.getType().equalsIgnoreCase(ServiceType.LODGING.toString())) {
            ServiceGetter homeSvc = searchForService(student.getHome());
            if (homeSvc != null && homeSvc.getPrice() > target.getPrice()) {
                if (target.getCapacity() > 0 && target.isFull()) return;
                setNewHome(name, target.getName());
                return;
            }
        }

        if (current != null) current.removeUser(student);
        target.addUser(student);
        student.setLocation(target.getName());                    // canonical name

        if (student.getType().equalsIgnoreCase(StudentType.OUTGOING.toString())) {
            student.addVisitedLocation(target);
        } else if (student.getType().equalsIgnoreCase(StudentType.BOOKISH.toString())) {
            if (target.getType().equalsIgnoreCase(ServiceType.LEISURE.toString())) student.addVisitedLocation(target);
        } else if (student.getType().equalsIgnoreCase(StudentType.THRIFTY.toString())) {
            if (target.getType().equalsIgnoreCase(ServiceType.EATING.toString())) student.addVisitedLocation(target);
        }
    }
    @Override
    public boolean moreExpensive(String name, String location) {
        // if service doesn't exist, return false
        ServiceGetter targetServiceGetter = searchForService(location);
        // Leisure services are never compared for "expensive"
        if (targetServiceGetter instanceof Leisure) {
            return false;
        }
        // Case 1: Eating service comparison
        if (targetServiceGetter instanceof Eating) {
            double newLocationPrice = targetServiceGetter.getPrice();

            Iterator<ServiceGetter> visited = getVisitedPlaces(name);
            while (visited.hasNext()) {
                ServiceGetter place = visited.next();
                if (place instanceof Eating && place.getPrice() < newLocationPrice) {
                    return true;
                }
            }
            return false;
        }

        // Case 2: Lodging or other service types
        StudentGetter studentGetter = searchForStudent(name);

        String homeName = studentGetter.getHome();
        ServiceGetter homeServiceGetter = searchForService(homeName);

        double homePrice = homeServiceGetter.getPrice();
        double newLocationPrice = targetServiceGetter.getPrice();
        return newLocationPrice > homePrice;
    }
    /**
     * Gets a student by name from the students' collection.
     *
     * @param name Student's name
     * @return Student object if found, null otherwise
     */
    @Override
    public StudentGetter getStudentByName(String name) {
        return searchForStudent(name);
    }

    @Override
    public ServiceGetter getStudentLocation(String name) {
        StudentGetter studentGetter = getStudentByName(name);
        String locationName = studentGetter.getLocation();
        // location is stored canonically; keep the HOME fallback just in case
        if (locationName.equalsIgnoreCase(HomeType.HOME.toString())) {
            locationName = studentGetter.getHome();
        }
        return getServiceByName(locationName);
    }

    @Override
    public String getStudentLodging(String name) {
        StudentGetter s = getStudentByName(name);
        return s == null ? null : s.getHome();
    }

    @Override
    public boolean hasAnyStudents() {
        return students.hasAnyStudents();
    }

    @Override
    public boolean isMovingOK(String name, String lodging) {
        StudentGetter studentGetter = searchForStudent(name);
        if (studentGetter == null) {
            return false; // Or handle as an error
        }

        if (!isStudentThrifty(name)) {
            return true;
        }

        ServiceGetter currentHomeServiceGetter = searchForService(studentGetter.getHome());
        ServiceGetter newLodgingServiceGetter = searchForService(lodging);

        // A move is not okay if either service doesn't exist
        if (currentHomeServiceGetter == null || newLodgingServiceGetter == null) {
            return false;
        }

        // For a thrifty student, the new home must not be more expensive.
        return newLodgingServiceGetter.getPrice() < currentHomeServiceGetter.getPrice();
    }

    @Override
    public void setNewHome(String name, String home) {
        homeAwayApp.student.StudentSetter student = (homeAwayApp.student.StudentSetter) searchForStudent(name);
        ServiceSetter newHome = (ServiceSetter) searchForService(home);                 // resolve canonical Service

        // If already there, nothing to do
        boolean alreadyThere = student.getLocation() != null &&
                student.getLocation().equalsIgnoreCase(newHome.getName());

        // Capacity gate for lodging/eating services
        if (!alreadyThere && newHome.getCapacity() > 0 && newHome.isFull()) {
            return; // do NOT change state when full
        }

        // Remove from old home users list (if any)
        ServiceSetter oldHomeService = (ServiceSetter) searchForService(student.getHome());
        if (oldHomeService != null) {
            oldHomeService.removeUser(student);
        }

        // Canonical fields and sync users list
        String canonical = newHome.getName();
        student.setNewHome(canonical);
        student.setLocation(canonical);
        newHome.addUser(student);

        if (student.getType().equalsIgnoreCase(StudentType.OUTGOING.toString())) {
            student.addVisitedLocation(newHome);
        }
    }


    @Override
    public boolean isRatingInvalid(int stars) {
        return stars > 5 || stars < 1;
    }

    @Override
    public void registerRating(ServiceSetter service, int stars, String description) {
        services.registerRating(service, stars, description);
    }

    @Override
    public boolean validServiceType(String serviceType) {
        return ServiceType.fromString(serviceType) != null;
    }

    @Override
    public boolean validStudentType(String studentType) { return StudentType.fromString(studentType) != null; }

    @Override
    public Iterator<ServiceGetter> getAllServices() {
        return services.getAllServices();
    }

    @Override
    public Iterator<StudentGetter> getAllStudents() {
        return students.getStudents();
    }

    @Override
    public boolean hasAnyServices() {
        return services.hasAnyService();
    }

    @Override
    public boolean isStudentThrifty(String name) {
        StudentGetter studentGetter = searchForStudent(name);
        return studentGetter instanceof ThriftyClass;
    }

    @Override
    public boolean hasServiceWithStars(String serviceType, int rank) {
        Iterator<ServiceGetter> byRank = services.getServicesGivenARating(rank);
        while (byRank.hasNext()) {
            ServiceGetter place = byRank.next();
            if (place.getType().equalsIgnoreCase(serviceType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasVisitedLocations(String name) {
        StudentGetter studentGetter = searchForStudent(name);
        return studentGetter.hasVisitedPlaces();
    }

    @Override
    public Iterator<ServiceGetter> getVisitedPlaces(String name) {
        StudentGetter studentGetter = searchForStudent(name);
        return studentGetter.getVisited();
    }



    @Override
    public void listUsers(String order, String serviceName) {
        ServiceGetter target = getServiceByName(serviceName);
        Iterator<StudentGetter> it =
                order.equals(">") ? target.studentsIterator() : target.studentsReverseIterator();

        if (!it.hasNext()) {
            System.out.printf(String.format(MSG.NO_STUDENTS_ON_SERVICE.toString(), target.getName()));
            return;
        }

        // There is at least one student: print the first, then the rest
        while (it.hasNext()) {
            StudentGetter s = it.next();
            System.out.printf(String.format(MSG.LIST_USERS_ON_SERVICE.toString(),
                    s.getName(), s.getType().toLowerCase()));
        }
    }

    @Override
    public String getRecommendedService(String name, String type) {
        StudentGetter studentGetter = searchForStudent(name);
        boolean thrifty = (studentGetter instanceof ThriftyClass);
        ServiceGetter best = null;

        dataStructures.Iterator<ServiceGetter> it = getAllServices();
        while (it.hasNext()) {
            ServiceGetter s = it.next();
            if (!s.getType().equalsIgnoreCase(type)) continue;

            if (best == null || isBetter(s, best, thrifty)) {
                best = s;
            }
        }
        return (best != null) ? best.getName() : null; // existing code assumes it's never null
    }

    private boolean isBetter(ServiceGetter cand, ServiceGetter best, boolean thrifty) {
        if (thrifty) {
            // Thrifty: lowest effective price, then earliest insertion
            int priceCand = effectivePriceForThrifty(cand);
            int priceBest = effectivePriceForThrifty(best);

            if (priceCand < priceBest) return true;
            if (priceCand > priceBest) return false;

            // same price → earlier insertion wins
            return getInsertionIndex(cand) < getInsertionIndex(best);
        } else {
            // Bookish/Outgoing: highest average, then earliest insertion, then oldest average
            int avgCand = cand.getAvgRounded();
            int avgBest = best.getAvgRounded();

            if (avgCand > avgBest) return true;
            if (avgCand < avgBest) return false;

            int idxCand = getInsertionIndex(cand);
            int idxBest = getInsertionIndex(best);

            if (idxCand < idxBest) return true;
            if (idxCand > idxBest) return false;

            long whenCand = cand.getAvgRoundedUpdateOrder();
            long whenBest = best.getAvgRoundedUpdateOrder();

            return whenCand < whenBest; // older = smaller = better
        }
    }


    // Helper: effective price per type (integer arithmetic; no rounding surprises)
    private int effectivePriceForThrifty(ServiceGetter s) {
        int base =  s.getPrice();
        if (s.getType().equalsIgnoreCase(ServiceType.LEISURE.toString())) {
            // For leisure services, apply student discount
            int discount = s.getDiscount();
            base = (base * (100 - discount)) / 100;
        }
        return base;
    }

    @Override
    public boolean hasServicesOfType(String type) {
        Integer count = serviceCountByType.get(type);
        return count != null && count > 0;
    }

    /**
     * Finds a service.
     * Searches for a service given its <code>name</code>
     * and returns the Service object.
     * Returns <code>null</code> if a service with the given <code>name</code> doesn't exist.
     *
     * @param name the <code>name</code> of the service to be found.
     * @return the service or <code>null</code> if it doesn't exist.
     */
    private ServiceGetter searchForService(String name) {
        String key = keyof(name);
        if (key == null) return null;
        return servicesMap.get(key);
    }


    /**
     * Finds a student.
     * Searches for a student given its <code>name</code>
     * and returns the Student object.
     * Returns <code>null</code> if a student with the given <code>name</code> doesn't exist.
     *
     * @param name the <code>name</code> of the student to be found.
     * @return the student or <code>null</code> if it doesn't exist.
     */
    private StudentGetter searchForStudent(String name) {
        String key = keyof(name);
        if (key == null) return null;
        return studentsMap.get(key);
    }


    @Override
    public void listServicesWithTag(String word) {
        dataStructures.DoublyLinkedList<ServiceGetter> insertion = services.getInsertionList(); // Iterate services in insertion order
        dataStructures.Iterator<ServiceGetter> it = insertion.iterator();

        boolean printedAny = false;
        while (it.hasNext()) {
            ServiceGetter s = it.next();
            if (s != null && s.hasTag(word)) {
                System.out.printf(String.format(MSG.LIST_SERVICE_WITH_TAG.toString(), s.getType().toLowerCase(), s.getName()));   // Spec: print "<type> <name>"
                printedAny = true;
            }
        }
        if (!printedAny) {
            System.out.println(MSG.NO_SERVICE_WITH_TAG.toString());
        }
    }



    // Case-insensitive finder that returns the stored Service (canonical name)
    public ServiceGetter getServiceByName(String name) {
        return searchForService(name);
    }

    @Override
    public Iterator<StudentGetter> getStudentsByCountry(String country) {
        DoublyLinkedList<StudentGetter> studentsFromCountry = new DoublyLinkedList<>();
        Iterator<StudentGetter> allStudents = getAllStudents();

        while (allStudents.hasNext()) {
            StudentGetter studentGetter = allStudents.next();
            // Compare with the student's country field, not home lodging
            if (studentGetter.getCountry() != null &&
                    studentGetter.getCountry().equalsIgnoreCase(country)) {
                studentsFromCountry.addLast(studentGetter);
            }
        }
        return studentsFromCountry.iterator();
    }

    @Override
    public void listServicesByRanking() {
        DoublyLinkedList<ServiceGetter>[] buckets = services.getBuckets();

        for (int idx = 4; idx >= 0; idx--) {
            DoublyLinkedList<ServiceGetter> bucket = buckets[idx];
            if (bucket == null || bucket.isEmpty()) continue;
            dataStructures.TwoWayIterator<ServiceGetter> it = bucket.twoWayiterator();
            it.rewind();                      // position before first
            while (it.hasNext()) {            // oldest first (CORRECT)
                ServiceGetter s = it.next();
                System.out.printf(String.format(MSG.LIST_SERVICE_RAKING.toString(), s.getName(), s.getAvgRounded()));
            }
        }
    }

    @Override
    public void listRankedByTypeAndAverage(String type, int rank, String studentName) {
        // 1) Get student's current location
        ServiceGetter studentLoc = getStudentLocation(studentName);
        if (studentLoc == null) return;
        long sLat = studentLoc.getLatitude();
        long sLon = studentLoc.getLongitude();

        // 2) First pass: compute best (minimum) Manhattan distance among services of given type and rank
        Iterator<ServiceGetter> bucket = services.getServicesGivenARating(rank);
        long best = Long.MAX_VALUE;
        while (bucket.hasNext()) {
            ServiceGetter svc = bucket.next();
            if (!svc.getType().equalsIgnoreCase(type)) continue;
            long d = manhattan(sLat, sLon, svc.getLatitude(), svc.getLongitude());
            if (d < best) best = d;
        }
        if (best == Long.MAX_VALUE) return;

        // 3) Second pass: collect all services at best distance
        bucket = services.getServicesGivenARating(rank);
        DoublyLinkedList<ServiceGetter> ties = new DoublyLinkedList<>();
        while (bucket.hasNext()) {
            ServiceGetter svc = bucket.next();
            if (!svc.getType().equalsIgnoreCase(type)) continue;
            long d = manhattan(sLat, sLon, svc.getLatitude(), svc.getLongitude());
            if (d == best) {
                ties.addLast(svc);
            }
        }

        // 4) IMPORTANT FIX: Sort by insertion order (not update order) for ranked command
        // Convert to array for sorting
        ServiceGetter[] tiesArray = new ServiceGetter[ties.size()];
        Iterator<ServiceGetter> tiesIt = ties.iterator();
        int idx = 0;
        while (tiesIt.hasNext()) {
            tiesArray[idx++] = tiesIt.next();
        }

        // Sort by insertion index (earlier insertion first)
        for (int i = 1; i < tiesArray.length; i++) {
            ServiceGetter key = tiesArray[i];
            int keyIdx = getInsertionIndex(key);

            int j = i - 1;
            while (j >= 0) {
                ServiceGetter cur = tiesArray[j];
                int curIdx = getInsertionIndex(cur);

                if (curIdx <= keyIdx) break; // keep stable order when equal
                tiesArray[j + 1] = cur;
                j--;
            }
            tiesArray[j + 1] = key;
        }

        // 5) Print winners (names only)
        for (ServiceGetter s : tiesArray) {
            System.out.println(s.getName());
        }
    }

    // Helper: Manhattan distance using latitude and longitude (long)
    private static long manhattan(long lat1, long lon1, long lat2, long lon2) {
        long dLat = Math.abs(lat1 - lat2);
        long dLon = Math.abs(lon1 - lon2);
        return dLat + dLon;
    }

    // Helper: position of a service in the global insertion order list
    public int getInsertionIndex(ServiceGetter s) {
        DoublyLinkedList<ServiceGetter> insertion = services.getInsertionList();
        int idx = insertion.indexOf(s);
        return (idx >= 0) ? idx : Integer.MAX_VALUE; // fallback if not found
    }

    // HashMao jey Normalization helper
    private String keyof(String name){
        if (name == null) return null;
        return name.trim().toLowerCase();
    }

    // rebuild indexeso of HashMaps
    private void rebuildIndexes(){
        this.studentsMap = new SepChainHashTable<>();
        this.servicesMap = new SepChainHashTable<>();
        // NEW: reset count map
        this.serviceCountByType = new SepChainHashTable<>();
        // Rebuild Service index
        Iterator<ServiceGetter> itS = services.getAllServices();
        while (itS.hasNext()){
            ServiceGetter itServiceGetter = itS.next();
            String key = keyof(itServiceGetter.getName());
            if (key!=null) {
                servicesMap.put(key, itServiceGetter);
            }
            // NEW: rebuild counts
            String typeKey = itServiceGetter.getType();
            Integer count = serviceCountByType.get(typeKey);
            if (count == null) {
                serviceCountByType.put(typeKey, 1);
            } else {
                serviceCountByType.put(typeKey, count + 1);
            }
        }
        // Rebuild Student index
        Iterator<StudentGetter> itStudent = students.getStudents();
        while (itStudent.hasNext()){
            StudentGetter studentGetter = itStudent.next();
            String key = keyof(studentGetter.getName());
            if (key!=null) {
                studentsMap.put(key, studentGetter);
            }
        }
    }

    /**
     * Checks if a lodging service is full when counting only its residents
     * (students whose home is this lodging), ignoring temporary visitors.
     *
     * @param lodging the lodging service to test
     * @return true if the number of resident students is
     *         greater than or equal to the lodging's capacity (and capacity > 0),
     *         false otherwise
     */
    @Override
    public boolean isLodgingFullByResidents(ServiceGetter lodging) {
        // Predicate that checks if a student has this lodging as home
        Predicate<StudentGetter> livesHere = new Predicate<StudentGetter>() {
            @Override
            public boolean check(StudentGetter s) {
                String h = s.getHome();
                return h != null &&
                        lodging.getName().equalsIgnoreCase(h);
            }
        };
        Iterator<StudentGetter> it = new FilterIterator<StudentGetter>(getAllStudents(), livesHere);
        int residents = 0;
        while (it.hasNext()) {
            it.next();
            residents++;
        }
        return residents >= lodging.getCapacity();
    }
}

