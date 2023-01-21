import java.util.ArrayList;

public class LaneData {
    public Vector vector;
    public ArrayList<Position> intersections;

    public LaneData(Vector vector) {
        this.vector = vector;
        intersections = new ArrayList<Position>();
    }
}
