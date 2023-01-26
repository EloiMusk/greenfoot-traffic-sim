public enum Direction {
    NORTH, SOUTH, EAST, WEST;

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

