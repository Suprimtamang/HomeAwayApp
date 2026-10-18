package homeAwayApp.service;

import homeAwayApp.constants.ServiceType;

/**
 * LeisureServiceClass represents a Service of type Leisure.
 * Implements LeisureService interface.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public class LeisureClass extends AbstractService implements Leisure {

    /**
     * Constructs a new LeisureServiceClass.
     *
     * @param latitude        Service's latitude
     * @param longitude       Service's longitude
     * @param ticketPrice     The price a ticket to the service costs
     * @param studentDiscount The student discount that can be applied to the ticket price
     * @param name            The Service's name
     */
    public LeisureClass(long latitude, long longitude, int ticketPrice, int studentDiscount, String name) {
        super(name, longitude, latitude);
        this.price = ticketPrice;
        this.discount = studentDiscount;
        this.capacity = -1;
    }

    @Override
    public String getType() {
        return ServiceType.LEISURE.toString();
    }

    public int getCapacity() {
        return  -1;
    }

}
