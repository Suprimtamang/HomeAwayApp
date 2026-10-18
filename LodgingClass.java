package homeAwayApp.service;

import homeAwayApp.constants.ServiceType;

/**
 * LodgingServiceClass represents a Service of type Lodging.
 * Implements LodgingService interface.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public class LodgingClass extends AbstractService implements Lodging {

    /**
     * Constructs a new LodgingServiceClass.
     *
     * @param latitude  Service's latitude
     * @param longitude Service's longitude
     * @param roomPrice Service's price
     * @param name      Service's name
     */
    public LodgingClass(long latitude, long longitude, int roomPrice, int capacity, String name) {
        super(name, longitude, latitude);
        this.price = roomPrice;
        this.capacity = capacity;       // must be > 0 (validated in app)
        this.discount = 0;
    }

    @Override
    public String getType() {
        return ServiceType.LODGING.toString();
    }

    @Override
    public int getCapacity() {
        return super.getCapacity();
    }

}
