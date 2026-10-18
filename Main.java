import java.util.Scanner;

import dataStructures.*;
import homeAwayApp.app.HomeAwayAppClass;
import homeAwayApp.app.HomeAwayAppGetter;
import homeAwayApp.app.HomeAwayAppSetter;
import homeAwayApp.constants.*;
import homeAwayApp.service.Lodging;
import homeAwayApp.constants.ServiceType;
import homeAwayApp.service.ServiceSetter;
import homeAwayApp.student.StudentGetter;
import homeAwayApp.service.ServiceGetter;
import homeAwayApp.constants.StudentType;

/**
 * Main class of the HomeAway program.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 * Note: Some parts of the code were adapted from the previous year's OOP project provided by the course instructors.
 * @author Sofia Mercier (72591) s.mercier@campus.fct.unl.pt was one of the partner in previous year however she has no involvemnet  in this year project
 *
 * Note: Likewise, Used ChatGPT and GitHub Copilot for debugging and conceputal clarifications and code review process while implementing test class.
 **/

public class Main {

    public static void main(String[] args) {
        commands();
    }

    //Command interpreter
    private static void commands() {
        HomeAwayAppSetter homeAway = new HomeAwayAppClass();
        Scanner     in       = new Scanner(System.in);
        Command command;
        do {
            command = Command.valueOf(getCommand(in));
            switch (command) {
                case EXIT    -> exit(homeAway);
                case HELP    -> help();
                case BOUNDS  -> createArea(in, homeAway);
                case SAVE    -> saveArea(homeAway);
                case LOAD    -> loadArea(in, homeAway);
                case SERVICE -> addService(in, homeAway);
                case SERVICES-> listServices(homeAway);
                case STUDENT -> addStudent(in, homeAway);
                case LEAVE   -> removeStudent(in, homeAway);
                case STUDENTS-> listStudents(in, homeAway);
                case GO      -> changeStudentLocation(in, homeAway);
                case MOVE    -> changeStudentHome(in, homeAway);
                case USERS   -> studentUsers(in, homeAway);
                case STAR    -> star(in, homeAway);
                case WHERE   -> getStudentLocation(in, homeAway);
                case VISITED -> getVisitedLocation(in, homeAway);
                case RANKING -> listServicesByRanking(homeAway);
                case RANKED  -> getRankedServices(in, homeAway);
                case TAG     -> listServicesWithTag(in, homeAway);
                case FIND    -> findServiceForStudent(in, homeAway);
                default      -> System.out.println(MSG.UNKNOWN);
            }
        } while (command != Command.EXIT);
        in.close();
    }

    private static void exit(HomeAwayAppGetter homeAway) {
        if (homeAway.areBoundsDefined()) {
            homeAway.saveArea();
        }
        System.out.println(MSG.BYE);
    }

    private static void studentUsers(Scanner in, HomeAwayAppGetter homeAway) {
       String order = in.next().trim();
       String serviceName = in.nextLine().trim();
        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }
        if (!(order.equals("<") || order.equals(">"))) {
            System.out.println(MSG.ORDER_NOT_EXISTS);
            return;
        }
        ServiceGetter target = homeAway.getServiceByName(serviceName);
        if (target == null) {
            System.out.printf(String.format(MSG.NON_EXISTENT.toString(), serviceName));
            return;
        }
        if (target.getType().equalsIgnoreCase(ServiceType.LEISURE.toString())) {
            System.out.printf(String.format(MSG.NOT_CONTROL_MOVEMENT.toString(), target.getName()));
            return;
        }
        homeAway.listUsers(order, serviceName);
    }
    /**
     * Reads the command from the user input and sends it to the command interpreter.
     *
     * @param in the input source
     * @return command the command given to the program
     */
    private static String getCommand(Scanner in) {
        return in.next().trim().toUpperCase();
    }

    /**
     * Outputs information on what each command does.
     */
    private static void help() { Menu.showMenu(); }

    /**
     * Creates a new area within the specified bounds.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the area will be added to
     */
    private static void createArea(Scanner in, HomeAwayAppSetter homeAway) {
        long latitudeTop = in.nextLong();
        long longitudeLeft = in.nextLong();
        long latitudeBottom = in.nextLong();
        long longitudeRight = in.nextLong();
        String name = getNextLine(in);
        boolean sameAsCurrent = homeAway.areBoundsDefined() && name.equals(homeAway.getAreaName());
        if  (sameAsCurrent || homeAway.areaExists(name)){
            System.out.println(MSG.BOUNDS_NAME_EXISTS);
            return;
        }
        else if (!homeAway.areValidBounds(latitudeTop, longitudeLeft, latitudeBottom, longitudeRight)){
            System.out.println(MSG.INVALID_BOUNDS);
            return;
        }
      if( homeAway.areBoundsDefined()) {
          homeAway.saveArea();
        }
        homeAway.createArea(latitudeTop, longitudeLeft, latitudeBottom, longitudeRight, name);
        System.out.printf(String.format(MSG.AREA_CREATED.toString(), name));
    }

    /**
     * Creates a new eating, lodging, or leisure service, with the specified coordinates, price, and name.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the service will be added to
     */
    private static void addService(Scanner in, HomeAwayAppSetter homeAway) {
        String type = in.next().trim();
        ServiceType serviceType = ServiceType.fromString(type);
        long latitude = in.nextLong();
        long longitude = in.nextLong();
        int menuPrice = in.nextInt();
        int value = (in.nextInt()); // capacity or discount
        String name = in.nextLine().trim();
        ServiceGetter existing = homeAway.getServiceByName(name);    // Case-insensitive lookup to prevent duplicate names

        if (serviceType == ServiceType.EATING) {
            if (homeAway.isInValidPrice(menuPrice)) {
                System.out.println(MSG.INVALID_MENU_PRICE_MSG);
            } else if (homeAway.checkValue(value)) {
                System.out.println(MSG.INVALID_CAPACITY);
            } else if (!homeAway.isWithinBounds(latitude, longitude)) {
                System.out.printf(String.format(MSG.OUTSIDE_BOUNDS.toString(), name));
            } else if (existing != null) {
                System.out.printf(String.format(MSG.NAME_ALREADY_EXISTS.toString(), existing.getName()));
            } else {
                homeAway.addRestaurant(latitude, longitude, menuPrice, value, name);
                ServiceGetter canonical = homeAway.getServiceByName(name);
                System.out.printf(String.format(MSG.SERVICE_ADDED.toString(),ServiceType.EATING.toString().toLowerCase() ,canonical.getName()));
            }

        } else if (serviceType == ServiceType.LODGING) {
            if (homeAway.isInValidPrice(menuPrice)) {
                System.out.println(MSG.INVALID_ROOM_PRICE_MSG);
            } else if (homeAway.checkValue(value)) {
                System.out.println(MSG.INVALID_CAPACITY);
            } else if (!homeAway.isWithinBounds(latitude, longitude)) {
                System.out.printf(String.format(MSG.OUTSIDE_BOUNDS.toString(), name));
            } else if (existing != null) {
                System.out.printf(String.format(MSG.NAME_ALREADY_EXISTS.toString(), existing.getName()));
            } else {
                homeAway.addLodging(latitude, longitude, menuPrice, value, name);
                ServiceGetter canonical = homeAway.getServiceByName(name);
                System.out.printf(String.format(MSG.SERVICE_ADDED.toString(), ServiceType.LODGING.toString().toLowerCase(),canonical.getName()));
            }

        } else if (serviceType == ServiceType.LEISURE) {
            if (homeAway.isInValidPrice(menuPrice)) {
                System.out.println(MSG.INVALID_TICKET_PRICE_MSG);
            } else if (homeAway.checkDiscount(value)) {
                System.out.println(MSG.INVALID_DISCOUNT_MSG);
            } else if (!homeAway.isWithinBounds(latitude, longitude)) {
                System.out.printf(String.format(MSG.OUTSIDE_BOUNDS.toString(), name));
            } else if (existing != null) {
                System.out.printf(String.format(MSG.NAME_ALREADY_EXISTS.toString(), existing.getName()));
            } else {
                homeAway.addLeisure(latitude, longitude, menuPrice, value, name);
                ServiceGetter canonical = homeAway.getServiceByName(name);
                System.out.printf(String.format(MSG.SERVICE_ADDED.toString(),ServiceType.LEISURE.toString().toLowerCase() ,canonical.getName()));
            }
        } else {
            System.out.println(MSG.INVALID_SERVICE_TYPE);
        }
    }

    /**
     * Lists all services in the system.
     *
     * @param homeAway the HomeAwayApp instance the services are in
     */
    private static void listServices(HomeAwayAppGetter homeAway) {
        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }
        if (!homeAway.hasAnyServices()) {
            System.out.println(MSG.NO_SERVICE_YET);
        } else {
            Iterator<ServiceGetter> allServices = homeAway.getAllServices();
            while (allServices.hasNext()) {
                ServiceGetter serviceGetter = allServices.next();
                System.out.printf(String.format(Filter.LIST_SERVICES.toString(),
                        serviceGetter.getName(),
                        serviceGetter.getType().toLowerCase(),
                        serviceGetter.getLatitude(),
                        serviceGetter.getLongitude()));
            }
        }
    }

    /**
     * Adds a new student to the app.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the student will be added to
     */
    private static void addStudent(Scanner in, HomeAwayAppSetter homeAway) {
        String type = in.nextLine().trim();         // type
        String name = in.nextLine().trim();         // name
        String country = in.nextLine().trim();      // country
        String home = in.nextLine().trim();         // Lodging service

        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }
        StudentType studentType = StudentType.fromString(type);
        if (studentType == null) {
            System.out.println(MSG.INVALID_STUDENT_TYPE);
            return;
        }
        ServiceGetter stLodgingServiceGetter = homeAway.getServiceByName(home);
        if (!(stLodgingServiceGetter instanceof Lodging)) {
            System.out.printf(String.format(MSG.LODGING_SERVICE_NOT_EXISTS.toString(), home));
            return;
        }

        // NEW: capacity check here (before name uniqueness)
        if (homeAway.isLodgingFullByResidents(stLodgingServiceGetter)) {
            System.out.printf(String.format(MSG.SERVICE_IS_FULL.toString(),ServiceType.LODGING.toString().toLowerCase() ,home));
            return;
        }

        if (homeAway.hasStudent(name)) {
            System.out.printf(String.format(MSG.NAME_ALREADY_EXISTS.toString(), homeAway.getStudentByName(name).getName()));

        }
        else {
            homeAway.addStudent( type, name, country, home );
            StudentGetter added = homeAway.getStudentByName(name);
            System.out.printf(String.format(MSG.ADDED.toString(), added.getName()));
        }
    }
    /**
     * Removes a student from the app.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the student will be removed from
     */
    private static void removeStudent(Scanner in, HomeAwayAppSetter homeAway) {
        String name = in.nextLine().trim();
        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
        } else if (!homeAway.hasStudent(name)) {
            System.out.printf(String.format(MSG.NON_EXISTENT.toString(), name));
        } else {
            StudentGetter s = homeAway.getStudentByName(name);
            String displayName = s.getName();
            homeAway.removeStudent(name);
            System.out.printf(String.format(MSG.STUDENT_LEFT.toString(), displayName));
        }
    }

    /**
     * Lists all students in the system.
     *
     * @param homeAway the HomeAwayApp instance the students are in
     */
    private static void listStudents(Scanner in, HomeAwayAppGetter homeAway) {
        String filter = in.nextLine().trim();

        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }

        // Case 1: list all
        if (filter.isEmpty() || filter.equalsIgnoreCase(Filter.ALL.toString())) {
            if (!homeAway.hasAnyStudents()) {
                System.out.println(MSG.NO_STUDENTS); // "No students yet!"
                return;
            }

            Comparator<StudentGetter> byName = (a, b) -> a.getName().compareToIgnoreCase(b.getName());

            SortedList<StudentGetter> sorted = new SortedDoublyLinkedList<>(byName);
            Iterator<StudentGetter> it = homeAway.getAllStudents();
            while (it.hasNext()) sorted.add(it.next());

            Iterator<StudentGetter> allStudents = sorted.iterator();
            while (allStudents.hasNext()) {
                StudentGetter s = allStudents.next();
                System.out.printf(String.format(MSG.LIST_STUDENTS.toString(), s.getName(),
                        s.getType().toLowerCase(),
                        s.getLocation()));
            }
        }
        // Case 2: by country
        else {
            Iterator<StudentGetter> studentsByCountry = homeAway.getStudentsByCountry(filter);
            if (!studentsByCountry.hasNext()) {
                System.out.printf(String.format(MSG.NO_STUDENT_FROM.toString(), filter));
            } else {
                while (studentsByCountry.hasNext()) {
                    StudentGetter s = studentsByCountry.next();
                    System.out.printf(String.format(MSG.LIST_STUDENTS.toString(),
                            s.getName(),
                            s.getType().toLowerCase(),
                            s.getLocation()));
                }
            }
        }
    }


    /**
     * Changes the location of a student within the app.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the student is in
     */
    private static void changeStudentLocation(Scanner in, HomeAwayAppSetter homeAway) {
        String name = getNextLine(in);
        String location = getNextLine(in);

        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }

        if (!homeAway.hasStudent(name)) {
            System.out.printf(String.format(MSG.NON_EXISTENT.toString(), name));
            return;
        }

        if (!homeAway.hasService(location) && !location.equals(MSG.HOME.toString())) {
            System.out.printf(String.format(MSG.UNKNOWN_LOCATION.toString(), location));
            return;
        }

        if (homeAway.hasService(location) && homeAway.getServiceByName(location) instanceof Lodging) {
            System.out.printf(String.format(MSG.INVALID_SERVICE_LOCATION.toString(), location));
            return;
        }

        if (homeAway.getStudentLocation(name).getName().equals(location)
                || (homeAway.isStudentAtHome(name) && location.equals(MSG.HOME.toString()))) {
            System.out.println(MSG.ALREADY_THERE);
            return;
        }

        if (homeAway.hasService(location) && homeAway.getServiceByName(location).isFull()) {
            ServiceGetter serviceGetter = homeAway.getServiceByName(location);
            System.out.printf(String.format(MSG.SERVICE_IS_FULL.toString(), serviceGetter.getType().toLowerCase(), serviceGetter.getName()));
            return;
        }

        StudentGetter studentGetter = homeAway.getStudentByName(name);
        ServiceGetter target = homeAway.getServiceByName(location);
        String canonicalLocation = (target != null) ? target.getName() : location;

        // Print location update using canonical names
        System.out.printf(String.format(MSG.UPDATED_LOCATION.toString(), studentGetter.getName(), canonicalLocation));

        if (homeAway.isStudentThrifty(name) && homeAway.moreExpensive(name, canonicalLocation)) {
            System.out.printf(String.format(MSG.DISTRACTED.toString(), studentGetter.getName(), canonicalLocation));
        } else {
            System.out.println();
        }

        homeAway.updateStudentLocation(name, canonicalLocation);
    }

    /**
     * Changes the home of a student within the app.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the student and the lodging service are in
     */
 private static void changeStudentHome(Scanner in, HomeAwayAppSetter homeAway) {
       String name = in.nextLine().trim();
       String home = in.nextLine().trim();

       if (!homeAway.areBoundsDefined()) {
           System.out.println(MSG.NON_DEFINED_BOUNDS);
           return;
       }

       if (!homeAway.hasStudent(name)) {
           System.out.printf(String.format(MSG.NON_EXISTENT.toString(), name));
           return;
       }

       StudentGetter studentGetter = homeAway.getStudentByName(name);
       String displayName = studentGetter.getName();

       ServiceGetter newHomeServiceGetter = homeAway.getServiceByName(home);
       if (!(newHomeServiceGetter instanceof Lodging)) {
           System.out.printf(String.format(MSG.LODGING_SERVICE_NOT_EXISTS.toString(), home));
           return;
       }

       String canonicalHomeName = newHomeServiceGetter.getName();

       if (homeAway.getStudentLodging(name).equalsIgnoreCase(home)) {
           System.out.printf(String.format(MSG.ALREADY_HOME.toString(), displayName));   // Check if already home
           return;
       }

       // Check if move is acceptable for this student type FIRST
       if (!homeAway.isMovingOK(name, home)) {
           System.out.printf(String.format(MSG.NO_MOVING.toString(), displayName));
           return;
       }

       // Checking capacity AFTER checking move acceptability
       //Checking if there's space OR if student is already counted in this service
       ServiceGetter currentHome = homeAway.getServiceByName(studentGetter.getHome());
       boolean sameLocation = (currentHome != null &&
                              currentHome.getName().equalsIgnoreCase(canonicalHomeName));
     // If the student is not already “home” there and the lodging is full, block the move
       if (!sameLocation &&  homeAway.isLodgingFullByResidents(newHomeServiceGetter)) {
           System.out.printf(String.format(MSG.SERVICE_IS_FULL.toString(),ServiceType.LODGING.toString().toLowerCase() ,canonicalHomeName));
           return;
       }
       homeAway.setNewHome(name, home);  // Perform the move
       System.out.printf(String.format(MSG.SUCCESS_MOVING.toString(), canonicalHomeName, displayName, displayName));
   }

    /**
     * Allows users to anonymously rate a service.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the service is in
     */
    private static void star(Scanner in, HomeAwayAppSetter homeAway) {
        int stars = in.nextInt();
        String Name = in.nextLine().trim(); // to remove the leading space input 9 tests this
        String description = in.nextLine();

        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }

        if (homeAway.isRatingInvalid(stars)){
            System.out.println(MSG.INVALID_EVALUATION);
            return;
        }

        ServiceSetter service = (ServiceSetter) homeAway.getServiceByName(Name);
        if (service == null) {
            System.out.printf(String.format(MSG.NON_EXISTENT.toString(), Name));
            return;
        }

        homeAway.registerRating(service, stars, description);
        System.out.println(MSG.EVALUATION_REGITERED);
    }


    /**
     * Gets the location of a student.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the student is in
     */
    private static void getStudentLocation(Scanner in, HomeAwayAppGetter homeAway) {
        String studentName = getNextLine(in);
        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }
        if (!homeAway.hasStudent(studentName)) {
            System.out.printf(String.format(MSG.NON_EXISTENT.toString(), studentName));
            return;
        }
        StudentGetter studentGetter = homeAway.getStudentByName(studentName);
        ServiceGetter location = homeAway.getStudentLocation(studentName);
        if (location == null) {
            System.out.printf(String.format(MSG.UNKNOWN_LOCATION.toString(), studentGetter.getLocation()));
        } else {
            long x = location.getLatitude();
            long y = location.getLongitude();
            System.out.printf(String.format(MSG.LOCATION_MSG.toString(), studentGetter.getName(), location.getName(),location.getType().toLowerCase(), x, y));
        }
    }

    /**
     * Returns a list of locations visited by the specified student,
     * provided the student is not marked as thrifty.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the student and services are in
     */
    private static void getVisitedLocation(Scanner in, HomeAwayAppGetter homeAway) {
        String name = getNextLine(in);
        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }
        if (!homeAway.hasStudent(name)) {
            System.out.printf(String.format(MSG.NON_EXISTENT.toString(), name));
            return;
        }
        // get the canonical stored student object
        StudentGetter studentGetter = homeAway.getStudentByName(name);
        String canonicalName = studentGetter.getName();
        if (homeAway.isStudentThrifty(name)) {
            System.out.printf(String.format(MSG.VISITED_THRIFTY.toString(), canonicalName));
            return;
        }
        if (!homeAway.hasVisitedLocations(name)) {
            System.out.printf(String.format(MSG.NON_VISITED.toString(), canonicalName));
            return;
        }
        Iterator<ServiceGetter> iterator = homeAway.getVisitedPlaces(name);
        while (iterator.hasNext()) {
            ServiceGetter serviceGetter = iterator.next();
            System.out.println(serviceGetter.getName());
        }
    }

    /**
     * Lists all services by amount of stars in descending order.
     * Lists all services grouped by stars (5..1), preserving registration order.
     * @param homeAway the HomeAwayApp instance containing all services
     */
    private static void listServicesByRanking(HomeAwayAppGetter homeAway) {
        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }
        if (!homeAway.hasAnyServices()) {
            System.out.println(MSG.NO_SERVICES); // "No services in the system."
            return;
        }
        System.out.println(MSG.DESCENDING_ORDER);
        homeAway.listServicesByRanking();
    }

    /**
     * Lists all services of a given type with a certain ranking.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the services are in
     */
    private static void getRankedServices(Scanner in, HomeAwayAppGetter homeAway) {
        String type = in.next().trim().toUpperCase();
        int rank = in.nextInt();
        String student = in.hasNextLine() ? in.nextLine().trim() : "";
        if (student.isEmpty() && in.hasNextLine()) student = in.nextLine().trim();

        // 1) bounds
        if (!homeAway.areBoundsDefined()) { System.out.println(MSG.NON_DEFINED_BOUNDS); return; }
        // 2) stars
        if (homeAway.isRatingInvalid(rank)) { System.out.println(MSG.INVALID_STARS); return; }
        // 3) student exists
        if (!homeAway.hasStudent(student)) { System.out.printf(String.format(MSG.NON_EXISTENT.toString(), student)); return; }
        // 4) type valid
        if (!homeAway.validServiceType(type)) { System.out.println(MSG.INVALID_SERVICE_TYPE); return; }
        // 5) any services of that type?
        if (!homeAway.hasServicesOfType(type)) { System.out.printf(String.format(MSG.NO_SERVICES_OF_TYPE.toString(), type)); return; }
        // 6) any with that average?
        if (!homeAway.hasServiceWithStars(type, rank)) {
            System.out.printf(String.format(MSG.NO_SERVICES_WITH_STARS.toString(), type.toLowerCase())); return;
        }
        // Header (your constant)
        System.out.printf(String.format(MSG.SERVICES_WITH_AVERAGE.toString(), type.toLowerCase(), rank));

        // Delegate actual selection + printing to the app:
        homeAway.listRankedByTypeAndAverage(type, rank, student);
    }

    /**
     * Lists all services that have at least one review whose description contains the specified word.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the services are in
     */
    private static void listServicesWithTag(Scanner in, HomeAwayAppGetter homeAway) {
        String tag = in.nextLine().trim();
        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }
        homeAway.listServicesWithTag(tag);
    }

    /**
     * Finds the most relevant service for a student given its type.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance to consult the service and the student from
     */
 private static void findServiceForStudent(Scanner in, HomeAwayAppGetter homeAway) {
     String name = in.nextLine().trim();
     String serviceType = in.nextLine().trim().toUpperCase();

     if (!homeAway.areBoundsDefined()) {
         System.out.println(MSG.NON_DEFINED_BOUNDS);
     } else if (!homeAway.hasStudent(name)) {
         System.out.printf(String.format(MSG.NON_EXISTENT.toString(), name));
     } else if (!homeAway.hasServicesOfType(serviceType)) {
         System.out.printf(String.format(MSG.NO_SERVICES_OF_TYPE.toString(), serviceType.toLowerCase()));
     } else {
         String recommendation = homeAway.getRecommendedService(name, serviceType);
         System.out.println(recommendation);
     }
 }

    /**
     * Saves the current area to a file.
     *
     * @param homeAway the HomeAwayApp instance the area will be saved from
     */
    private static void saveArea(HomeAwayAppGetter homeAway) {
        if (!homeAway.areBoundsDefined()) {
            System.out.println(MSG.NON_DEFINED_BOUNDS);
            return;
        }
        if (!homeAway.saveArea()) {
            System.out.printf(MSG.SAVE_FAILED.toString());
            return;
        }
        System.out.printf(String.format(MSG.AREA_SAVED.toString(), homeAway.getAreaName()));
    }

    /**
     * Loads an area from a file.
     *
     * @param in       the input source
     * @param homeAway the HomeAwayApp instance the area will be loaded to
     */
    private static void loadArea(Scanner in, HomeAwayAppSetter homeAway) {

        String areaName = in.nextLine().trim();
        // If there's a current area, save it before loading the new one
        if (homeAway.areBoundsDefined()) {
            homeAway.saveArea();
        }
        // Load the new area
        if (!homeAway.loadArea(areaName)) {
            System.out.printf(MSG.LOAD_FAILED.toString());
            return;
        }
        System.out.printf(String.format(MSG.AREA_LOADED.toString(), homeAway.getAreaName()));
    }

    private static String getNextLine(Scanner in){
        return in.nextLine().trim();
    }
    private static long getNextLong(Scanner in){
        return in.nextLong();
    }
}
