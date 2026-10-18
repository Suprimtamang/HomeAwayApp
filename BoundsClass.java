package homeAwayApp.area;

import java.io.Serial;
import java.io.Serializable;
/**
 * Represents the bounds of the location the students and services are in.
 * Implements the BoundsGetter interface.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public class BoundsClass implements BoundsSetter, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The bounds' <code>name</code>.
     */
    private String name;

    /**
     * The bounds' highest latitude.
     */
    private long topLatitude;

    /**
     * The bounds' lowest longitude.
     */
    private long leftLongitude;

    /**
     * The bounds' lowest latitude.
     */
    private long bottomLatitude;

    /**
     * The bounds' highest longitude.
     */
    private long rightLongitude;

    /**
     * The constructor of the bounds class.
     */
     public BoundsClass(){
        this.topLatitude = 0;
        this.leftLongitude = 0;
        this.bottomLatitude = 0;
        this.rightLongitude = 0;
        this.name = "";
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean areValidBounds(long latitudeTop, long longitudeTop, long latitudeBottom, long longitudeRight) {
        return !((latitudeTop <= latitudeBottom) || (longitudeTop >= longitudeRight));
    }

    @Override
    public void resetBoundsName() {
        name = "";
    }


    @Override
    public boolean areBoundsDefined() {
        return !name.isEmpty();
    }

    @Override
    public void createArea(long latitudeTop, long longitudeLeft, long latitudeBottom, long longitudeRight, String name) {
        topLatitude = latitudeTop;
        leftLongitude = longitudeLeft;
        bottomLatitude = latitudeBottom;
        rightLongitude = longitudeRight;
        this.name = name;
    }

    @Override
    public boolean isWithinBounds(long latitude, long longitude) {
        boolean isValidLatitude = (latitude >= bottomLatitude && latitude <= topLatitude);
        boolean isValidLongitude = (longitude >= leftLongitude && longitude <= rightLongitude);
        return isValidLatitude && isValidLongitude;
    }

}
