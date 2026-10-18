package homeAwayApp.area;

/**
 * Interface defining geographic boundary operations for the HomeAway app.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public interface BoundsGetter {

    /**
     * Validates whether the given coordinates form a proper rectangle.
     *
     * @param latitudeTop    Top latitude of the boundary
     * @param longitude      Left longitude of the boundary
     * @param latitudeBottom Bottom latitude of the boundary
     * @param longitudeRight Right longitude of the boundary
     * @return <code>true</code> if the coordinates form a valid boundary,
     * <code>false</code> otherwise
     */
    boolean areValidBounds(long latitudeTop, long longitude, long latitudeBottom, long longitudeRight);

    /**
     * Checks if the bounds' attributes are defined or are empty.
     *
     * @return <code>true</code> if the bounds are defined,
     * <code>false</code> otherwise
     */
    boolean areBoundsDefined();

    /**
     * Checks if a given location is within the defined bounds.
     *
     * @param latitude  Location's latitude
     * @param longitude Location's longitude
     * @return <code>true</code> if the location is within bounds,
     * <code>false</code> otherwise
     */
    boolean isWithinBounds(long latitude, long longitude);

    /**
     * Gets the name of the bounds.
     *
     * @return Name of the bounds
     */
    String getName();
}
