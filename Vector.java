public class Vector {
    public Position a;
    public Position b;

    public Vector(Position a, Position b) {
        this.a = a;
        this.b = b;
    }

    public Vector(int x1, int y1, int x2, int y2) {
        this.a = new Position(x1, y1);
        this.b = new Position(x2, y2);
    }

    public int getDistance() {
        int x = b.x - a.x;
        int y = b.y - a.y;
        return (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public int getDirection() {
        int x = b.x - a.x;
        int y = b.y - a.y;
        return (int) Math.toDegrees(Math.atan2(y, x));
    }

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
