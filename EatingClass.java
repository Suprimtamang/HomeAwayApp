package homeAwayApp.service;

import homeAwayApp.constants.ServiceType;

/**
 * EatingServiceClass represents a Service of type Eating.
 * Implements EatingService interface.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public class EatingClass extends AbstractService implements Eating {

    /**
     * Constructs a new EatingServiceClass.
     *
     * @param latitude  The Service's latitude
     * @param longitude The Service's longitude
     * @param menuPrice The Service's menuPrice
     * @param name      The Service's name
     */
    public EatingClass(long latitude, long longitude, int menuPrice, String name, int capacity) {
        super(name, longitude, latitude);
        this.price = menuPrice;
        this.capacity = capacity;
        this.discount = 0;
    }

    @Override
    public String getType() {
        return ServiceType.EATING.toString();
    }


    public int getCapacity() {
        return super.getCapacity();
    }




}
