import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * The WayPoint class represents a single point on the map.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class WayPoint {
    /**
     * The location of the waypoint.
     */
    public Position location;
//    public WayPoint[] wayPoints = new WayPoint[4];

//    public Map<Direction, WayPoint> wayPointMap = new HashMap<>();

    /**
     * The type of the waypoint.
     */
    public WayPointType type;

    /**
     * Creates a new waypoint.
     *
     * @param location The location of the waypoint.
     */
    public WayPoint(Position location) {
        this.location = location;
    }

    /**
     * Returns the string representation of the waypoint.
     *
     * @return The string representation of the waypoint.
     */
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

    /**
     * Returns true if the waypoint is north of the current waypoint.
     *
     * @param wayPoint The waypoint to check.
     * @return True if the waypoint is north of the current waypoint.
     */
    public boolean isNorth(WayPoint wayPoint) {
        return wayPoint.location.y < this.location.y && wayPoint.location.x == this.location.x;
    }

    /**
     * Returns true if the waypoint is south of the current waypoint.
     *
     * @param wayPoint The waypoint to check.
     * @return True if the waypoint is south of the current waypoint.
     */
    public boolean isSouth(WayPoint wayPoint) {
        return wayPoint.location.y > this.location.y && wayPoint.location.x == this.location.x;
    }

    /**
     * Returns true if the waypoint is west of the current waypoint.
     *
     * @param wayPoint The waypoint to check.
     * @return True if the waypoint is west of the current waypoint.
     */
    public boolean isWest(WayPoint wayPoint) {
        return wayPoint.location.x < this.location.x && wayPoint.location.y == this.location.y;
    }

    /**
     * Returns true if the waypoint is east of the current waypoint.
     *
     * @param wayPoint The waypoint to check.
     * @return True if the waypoint is east of the current waypoint.
     */
    public boolean isEast(WayPoint wayPoint) {
        return wayPoint.location.x > this.location.x && wayPoint.location.y == this.location.y;
    }
}
