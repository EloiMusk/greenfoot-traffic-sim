/**
 * An enumeration of the four compass directions.
 * @author EloiMusk
 * @version 1.0
 */
public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    /**
     * Returns the opposite direction.
     * @return the opposite direction
     */
    public Direction reverseDirection() {
        switch (this) {
            case NORTH:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.NORTH;
            case EAST:
                return Direction.WEST;
            case WEST:
                return Direction.EAST;
        }
        return null;
    }
}

