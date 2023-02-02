/**
 * A vector is a line between two points as defined by two positions.
 * It can be used to calculate the distance between two points, the direction of the line,
 * the orientation of the line, and the intersection of two lines.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Vector {

    /**
     * The first position of the vector.
     */
    public Position a;

    /**
     * The second position of the vector.
     */
    public Position b;

    /**
     * Creates a new vector based on two positions.
     *
     * @param a The first position of the vector.
     * @param b The second position of the vector.
     */
    public Vector(Position a, Position b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Creates a new vector based on two coordinates.
     *
     * @param x1 The x coordinate of the first position.
     * @param y1 The y coordinate of the first position.
     * @param x2 The x coordinate of the second position.
     * @param y2 The y coordinate of the second position.
     */
    public Vector(int x1, int y1, int x2, int y2) {
        this.a = new Position(x1, y1);
        this.b = new Position(x2, y2);
    }

    /**
     * Calculates the distance between the two positions.
     *
     * @return The distance between the two positions.
     */
    public int getDistance() {
        int x = b.x - a.x;
        int y = b.y - a.y;
        return (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * Calculates the direction of the line.
     *
     * @return The direction of the line.
     */
    public int getDirection() {
        int x = b.x - a.x;
        int y = b.y - a.y;
        return (int) Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Calculates the orientation of the line.
     *
     * @return The orientation of the line.
     */
    public Direction getOrientation() {
        int direction = getDirection();
        if (direction >= 0 && direction < 45) {
            return Direction.EAST;
        } else if (direction >= 45 && direction < 135) {
            return Direction.SOUTH;
        } else if (direction >= 135 && direction < 225) {
            return Direction.WEST;
        } else if (direction >= 225 && direction < 315) {
            return Direction.NORTH;
        } else {
            return Direction.EAST;
        }
    }

    /**
     * Calculates the intersection of two lines.
     *
     * @param vector The vector to calculate the intersection with.
     * @return The intersection of the two lines.
     */
    public Position getIntersection(Vector vector) {
        int x1 = a.x;
        int y1 = a.y;
        int x2 = b.x;
        int y2 = b.y;
        int x3 = vector.a.x;
        int y3 = vector.a.y;
        int x4 = vector.b.x;
        int y4 = vector.b.y;
        int denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (denominator == 0) {
            return null;
        }
        int x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denominator;
        int y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denominator;
        return new Position(x, y);
    }
}
