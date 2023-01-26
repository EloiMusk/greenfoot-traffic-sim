import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

public class Environment extends World {
    public Graph roadNetwork = new Graph();
    private boolean initialized = false;

    public Environment() {
        super(600, 600, 1, false);
        setBackground("textures/environment/1.jpg");
    }

    public void act() {
        if (!initialized) {
            init();
            initialized = true;
        }
    }

    public void init() {
//        generateLanes();
//        getIntersections();
//        markIntersections();
//        drawLanes();
//        drawStreet();
        generateGraph();
        markWayPoints();
        connectWayPoints();
    }

    private void generateGraph() {
        Position oldPos = new Position(getWidth() / 2, getHeight());
        Position newPos = new Position(getWidth() / 2, getHeight());
        GreenfootImage background = getBackground();
        int roadWidth = 50;
//        background.drawLine(oldPos.x, oldPos.y, newPos.x, newPos.y);
        WayPoint oldWayPoint = new WayPoint(new Position(oldPos.x, oldPos.y));
        WayPoint newWayPoint = new WayPoint(new Position(newPos.x, newPos.y));
        int margin = 100;
        while (newPos.y > 0) {
            margin--;
            if (newPos.y % (Greenfoot.getRandomNumber(600) + 1) == 0 && margin <= 0 && newPos.y > 100) {
                margin = 100;

                Position verticalPosA = new Position(0, newPos.y);
                Position verticalPosB = new Position(0, newPos.y);
                newWayPoint = new WayPoint(new Position(newPos.x, newPos.y));
                roadNetwork.addNewEdge(new Edge(oldWayPoint, newWayPoint, Direction.NORTH), true);

                switch (Greenfoot.getRandomNumber(3)) {
//                    Left
                    case 0:
                        verticalPosA.x = 0;
                        roadNetwork.addNewEdge(new Edge(newWayPoint, new WayPoint(verticalPosA), Direction.WEST), Greenfoot.getRandomNumber(10) % 2 == 0);
                        break;
//                        Right
                    case 1:
                        verticalPosB.x = getWidth();
                        roadNetwork.addNewEdge(new Edge(newWayPoint, new WayPoint(verticalPosB), Direction.EAST), Greenfoot.getRandomNumber(10) % 2 == 0);
//                        Start and End WayPoints
                        break;
//                        Straight
                    case 2:
                        verticalPosA.x = 0;
                        verticalPosB.x = getWidth();
                        roadNetwork.addNewEdge(new Edge(newWayPoint, new WayPoint(verticalPosA), Direction.WEST), Greenfoot.getRandomNumber(10) % 2 == 0);
                        roadNetwork.addNewEdge(new Edge(newWayPoint, new WayPoint(verticalPosB), Direction.EAST), Greenfoot.getRandomNumber(10) % 2 == 0);
                        break;
                }
                oldPos.y = newPos.y;
                oldWayPoint = newWayPoint;
            }
            newPos.y--;
        }
        oldWayPoint = newWayPoint;
        newWayPoint = new WayPoint(new Position(newPos.x, newPos.y));
        roadNetwork.addNewEdge(new Edge(oldWayPoint, newWayPoint, Direction.NORTH), true);
        System.out.println(roadNetwork.toString());
    }


    private void connectWayPoints() {
        WayPoint[] wayPoints = roadNetwork.getVertices();
        for (WayPoint wayPoint : wayPoints) {
            for (Node edge : roadNetwork.getEdges(wayPoint)) {
                switch (edge.direction){
                    case NORTH:
                        getBackground().setColor(Color.RED);
                        getBackground().drawLine(wayPoint.location.x, wayPoint.location.y, edge.value.location.x, edge.value.location.y);
                        break;
                    case EAST:
                        getBackground().setColor(Color.GREEN);
                        getBackground().drawLine(wayPoint.location.x, wayPoint.location.y, edge.value.location.x, edge.value.location.y);
                        break;
                    case SOUTH:
                        getBackground().setColor(Color.BLUE);
                        getBackground().drawLine(wayPoint.location.x, wayPoint.location.y, edge.value.location.x, edge.value.location.y);

                        break;
                    case WEST:
                        getBackground().setColor(Color.YELLOW);
                        break;
                }
            }
        }
    }

    private void markWayPoints() {
        WayPoint[] wayPoints = roadNetwork.getVertices();
        System.out.println("WayPoints: " + wayPoints.length);
        for (WayPoint wayPoint : wayPoints) {
            getBackground().setColor(Color.WHITE);
            getBackground().fillRect(wayPoint.location.x - 5, wayPoint.location.y - 5, 10, 10);
        }
    }

//    private void resolveStartWayPoints() {
//        wayPoints.forEach(wayPoint ->
//                wayPoint.wayPoints.addAll(
//                        wayPoints
//                                .stream().filter(wayPoint::waypointFilter)
//                                .collect(Collectors.toList())));
////        Print the index of each waypoint with a list of the indexes of his waypoints
//        wayPoints.forEach(wayPoint -> {
//            System.out.print("[" + wayPoints.indexOf(wayPoint) + " : " +wayPoint.direction.name() + "] -> ");
//            wayPoint.wayPoints.forEach(wayPoint1 -> System.out.print("[" + wayPoints.indexOf(wayPoint1) + "]"));
//            System.out.println();
//        });
////        for (WayPoint wayPoint : wayPoints.stream().filter(wayPoint -> wayPoint.type == WayPointType.START).collect(Collectors.toList())) {
//////            Forwards horizontal Resolution
////            if (wayPoint.location.x <= getWidth() / 2)
////                wayPoints.stream().filter(wayPoint1 -> wayPoint1.type != WayPointType.START && wayPoint1.location.x == wayPoint.location.x).min(Comparator.comparingInt(wayPoint2 -> wayPoint2.location.y)).ifPresent(point -> wayPoint.wayPoints.add(point)
////                );
//////            Forwards vertical Resolution
////            if (wayPoint.location.y > getHeight() - 150)
////                wayPoints.stream().filter(wayPoint1 -> wayPoint1.type != WayPointType.START && wayPoint1.location.y == wayPoint.location.y).min(Comparator.comparingInt(wayPoint2 -> wayPoint2.location.x)).ifPresent(point -> wayPoint.wayPoints.add(point));
//////            Backwards horizontal Resolution
////            if (wayPoint.location.x > getWidth() / 2)
////                wayPoints.stream().filter(wayPoint1 -> wayPoint1.type != WayPointType.START && wayPoint1.location.x == wayPoint.location.x).max(Comparator.comparingInt(wayPoint2 -> wayPoint2.location.y)).stream().findFirst().ifPresent(point -> wayPoint.wayPoints.add(point));
//////            Backwards vertical Resolution
////            if (wayPoint.location.y < 10)
////                wayPoints.stream().filter(wayPoint1 -> wayPoint1.type != WayPointType.START && wayPoint1.location.y == wayPoint.location.y).max(Comparator.comparingInt(wayPoint2 -> wayPoint2.location.x)).stream().findFirst().ifPresent(point -> wayPoint.wayPoints.add(point));
////        }
//    }

//    private void drawStreet() {
//        int roadWidth = 50;
//        GreenfootImage background = getBackground();
//        background.setColor(Color.BLACK);
//        Position oldPos = new Position(getWidth() / 2, getHeight());
//        Position newPos = new Position(getWidth() / 2, getHeight());
//        Position verticalPosA = new Position(0, 0);
//        Position verticalPosB = new Position(0, 0);
////        background.drawLine(oldPos.x, oldPos.y, newPos.x, newPos.y);
//        int margin = 100;
//        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y), WayPointType.START, Direction.NORTH));
//        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y), WayPointType.END, Direction.SOUTH));
//        while (newPos.y > 0) {
//            margin--;
//            if (newPos.y % (Greenfoot.getRandomNumber(600) + 1) == 0 && margin <= 0 && newPos.y > 100) {
//                margin = 100;
//
//                verticalPosA.y = newPos.y;
//                verticalPosB.y = newPos.y;
//
//                switch (Greenfoot.getRandomNumber(3)) {
////                    Left
//                    case 0:
//                        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y + (roadWidth / 4)), WayPointType.INTERSECTION, Direction.SOUTH));
//                        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y - (roadWidth / 4)), WayPointType.INTERSECTION, Direction.SOUTH));
//                        background.setColor(Color.BLACK);
//                        verticalPosA.x = 0;
//                        verticalPosB.x = newPos.x;
////                        Start and End WayPoints
//                        wayPoints.add(new WayPoint(new Position(verticalPosA.x, newPos.y + (roadWidth / 4)), WayPointType.START, Direction.EAST));
//                        wayPoints.add(new WayPoint(new Position(verticalPosA.x, newPos.y - (roadWidth / 4)), WayPointType.END, Direction.WEST));
//                        break;
////                        Right
//                    case 1:
//                        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y - (roadWidth / 4)), WayPointType.INTERSECTION, Direction.NORTH));
//                        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y + (roadWidth / 4)), WayPointType.INTERSECTION, Direction.NORTH));
//                        background.setColor(Color.BLACK);
//                        verticalPosA.x = newPos.x;
//                        verticalPosB.x = getWidth();
////                        Start and End WayPoints
//                        wayPoints.add(new WayPoint(new Position(verticalPosB.x, newPos.y - (roadWidth / 4)), WayPointType.START, Direction.WEST));
//                        wayPoints.add(new WayPoint(new Position(verticalPosB.x, newPos.y + (roadWidth / 4)), WayPointType.END, Direction.EAST));
//                        break;
////                        Straight
//                    case 2:
//                        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y + (roadWidth / 4)), WayPointType.INTERSECTION, Direction.NORTH));
//                        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y - (roadWidth / 4)), WayPointType.INTERSECTION, Direction.NORTH));
//                        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y + (roadWidth / 4)), WayPointType.INTERSECTION, Direction.EAST));
//                        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y - (roadWidth / 4)), WayPointType.INTERSECTION, Direction.EAST));
//                        background.setColor(Color.BLACK);
//                        verticalPosA.x = 0;
//                        verticalPosB.x = getWidth();
//                        wayPoints.add(new WayPoint(new Position(verticalPosA.x, newPos.y + (roadWidth / 4)), WayPointType.START, Direction.EAST));
//                        wayPoints.add(new WayPoint(new Position(verticalPosA.x, newPos.y - (roadWidth / 4)), WayPointType.END, Direction.WEST));
//                        wayPoints.add(new WayPoint(new Position(verticalPosB.x, newPos.y - (roadWidth / 4)), WayPointType.START, Direction.WEST));
//                        wayPoints.add(new WayPoint(new Position(verticalPosB.x, newPos.y + (roadWidth / 4)), WayPointType.END, Direction.EAST));
//                        break;
//                }
//                background.fillRect(verticalPosA.x, verticalPosA.y - (roadWidth / 2), (verticalPosA.x - verticalPosB.x) * -1, roadWidth);
//                background.setColor(Color.WHITE);
//                background.drawLine(verticalPosA.x + (roadWidth / 2), verticalPosA.y, verticalPosB.x - (roadWidth / 2), verticalPosB.y);
////                Fill intersection like this:
//                background.setColor(Color.BLACK);
////                background.fillRect(newPos.x - (roadWidth / 2), newPos.y - (roadWidth / 2), roadWidth, roadWidth);
//                background.fillRect(newPos.x - (roadWidth / 2), newPos.y, roadWidth, Math.subtractExact(oldPos.y, newPos.y));
//                background.setColor(Color.WHITE);
//                background.drawLine(oldPos.x, oldPos.y - (roadWidth / 2), newPos.x, newPos.y + (roadWidth / 2));
//                oldPos.y = newPos.y;
//            }
//            newPos.y--;
//        }
//        background.setColor(Color.BLACK);
//        background.fillRect(newPos.x - (roadWidth / 2), newPos.y, roadWidth, Math.subtractExact(oldPos.y, newPos.y));
//        background.setColor(Color.WHITE);
//        background.drawLine(oldPos.x, oldPos.y - (roadWidth / 2), newPos.x, newPos.y);
//        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y), WayPointType.START, Direction.SOUTH));
//        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y), WayPointType.END, Direction.NORTH));
//    }

//    private void drawLanes() {
//        ArrayList<LaneData> lanes = new ArrayList<LaneData>();
//        lanes.addAll(horizontalLanes);
//        lanes.addAll(verticalLanes);
//        for (LaneData lane : lanes) {
//            Lane laneActor = new Lane(lane.vector.a, lane.vector.b);
//            laneActor.intersections = lane.intersections;
//            addObject(laneActor, 0, 0);
//        }
//    }
//
//    private void markIntersections() {
//        int radius = 80;
//        for (LaneData lane : horizontalLanes) {
//            for (Position intersection : lane.intersections) {
//                getBackground().setColor(Color.RED);
//                getBackground().fillOval(intersection.x - radius, intersection.y - radius / 2, radius / 2, radius);
//                getBackground().setColor(Color.YELLOW);
//                getBackground().fillOval(intersection.x + radius / 2, intersection.y - radius / 2, radius / 2, radius);
//            }
//        }
//        for (LaneData lane : verticalLanes) {
//            for (Position intersection : lane.intersections) {
//                getBackground().setColor(Color.BLUE);
//                getBackground().fillOval(intersection.x - radius / 2, intersection.y - radius, radius, radius / 2);
//                getBackground().setColor(Color.GREEN);
//                getBackground().fillOval(intersection.x - radius / 2, intersection.y + radius / 2, radius, radius / 2);
//            }
//        }
//
//    }
//
//
//    public void generateLanes() {
//        Random rand = new Random();
//        int numberOfVerticalLanes = rand.nextInt(1) + 1;
//        int numberOfHorizontalLanes = rand.nextInt(2) + 1;
//        int numberOfLanes = numberOfVerticalLanes + numberOfHorizontalLanes;
//        int oldX = 0;
//        int oldY = 0;
////        Generate horizontal lanes.
//        for (int i = 0; i < numberOfHorizontalLanes; i++) {
//            int x1 = 0;
////            A number between 0 and 600.
//            int y1 = oldY + rand.nextInt(((600 - (50 * numberOfHorizontalLanes)) / numberOfHorizontalLanes) - 25) + 75;
//            oldY = y1;
//            int x2 = getWidth();
//            int y2 = y1;
//            horizontalLanes.add(new LaneData(new Vector(new Position(x1, y1), new Position(x2, y2))));
//        }
////        Generate vertical lanes.
//        for (int i = 0; i < numberOfVerticalLanes; i++) {
//            int x1 = oldX + rand.nextInt(((600 - (50 * numberOfVerticalLanes)) / numberOfVerticalLanes) - 25) + 75;
//            oldX = x1;
//            int y1 = 0;
//            int x2 = x1;
//            int y2 = getHeight();
//            verticalLanes.add(new LaneData(new Vector(new Position(x1, y1), new Position(x2, y2))));
//        }
//    }
//
//    public void getIntersections() {
//        for (LaneData horizontalLane : horizontalLanes) {
//            for (LaneData verticalLane : verticalLanes) {
//                Position intersection = horizontalLane.vector.getIntersection(verticalLane.vector);
//                if (intersection != null) {
//                    horizontalLane.intersections.add(intersection);
//                    verticalLane.intersections.add(intersection);
//                }
//            }
//        }
//    }
}
