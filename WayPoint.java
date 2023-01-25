import java.util.ArrayList;

public class WayPoint {
    public Position location;
    public ArrayList<WayPoint> wayPoints = new ArrayList<WayPoint>();
    public WayPointType type;

    public WayPoint(Position location, WayPointType type) {
        this.location = location;
        this.type = type;
    }
}
