public class Vector {
    public Position a;
    public Position b;
    public Vector(Position a, Position b) {
        this.a = a;
        this.b = b;
    }

    public int getLength() {
        int x = b.x - a.x;
        int y = b.y - a.y;
        return (int) Math.sqrt(x * x + y * y);
    }

    public int getDirection() {
        int x = b.x - a.x;
        int y = b.y - a.y;
        return (int) Math.toDegrees(Math.atan2(y, x));
    }
}
