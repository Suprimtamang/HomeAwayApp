package homeAwayApp.area;

public interface BoundsSetter extends BoundsGetter {

    /**
     * Creates a rectangular geographic area bounded by the given coordinates.
     *
     * @param latitudeTop    Top latitude of the boundary
     * @param longitudeLeft  Left longitude of the boundary
     * @param latitudeBottom Bottom latitude of the boundary
     * @param longitudeRight Right longitude of the boundary
     * @param name           Name of the place
     */
    void createArea(long latitudeTop, long longitudeLeft, long latitudeBottom, long longitudeRight, String name);

    /**
     * Resets the name of the bounds to an empty String.
     */
    void resetBoundsName();

}
