import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WayPoint {
    public Position location;
//    public WayPoint[] wayPoints = new WayPoint[4];

//    public Map<Direction, WayPoint> wayPointMap = new HashMap<>();

    public WayPointType type;

    public WayPoint(Position location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "[ X: " + this.location.x + " | Y: " + this.location.y + " ]";
    }


//    public Boolean waypointFilter(WayPoint wayPoint) {
//        if (wayPoint == null || wayPoint.location == null || wayPoint.type == null || wayPoint.type == WayPointType.START) {
//            return false;
//        }
//        boolean result = false;
//        switch (this.type) {
//            case END:
//                break;
//            case START:
//                switch (this.direction) {
//                    case NORTH:
//                        result = isNorth(wayPoint);
//                        break;
//                    case SOUTH:
//                        result = isSouth(wayPoint);
//                        break;
//                    case WEST:
//                        result = isWest(wayPoint);
//                        break;
//                    case EAST:
//                        result = isEast(wayPoint);
//                        break;
//                }
//                break;
//            case INTERSECTION:
//                switch (this.direction) {
//                    case NORTH:
//                        result = (isEast(wayPoint) || isWest(wayPoint) || isNorth(wayPoint));
//                        break;
//                    case SOUTH:
//                        result = (isEast(wayPoint) || isWest(wayPoint) || isSouth(wayPoint));
//                        break;
//                }
//                break;
//        }
//
//        return result;
//    }

    public boolean isNorth(WayPoint wayPoint) {
        return wayPoint.location.y < this.location.y && wayPoint.location.x == this.location.x;
    }

    public boolean isSouth(WayPoint wayPoint) {
        return wayPoint.location.y > this.location.y && wayPoint.location.x == this.location.x;
    }

    public boolean isWest(WayPoint wayPoint) {
        return wayPoint.location.x < this.location.x && wayPoint.location.y == this.location.y;
    }

    public boolean isEast(WayPoint wayPoint) {
        return wayPoint.location.x > this.location.x && wayPoint.location.y == this.location.y;
    }
}
