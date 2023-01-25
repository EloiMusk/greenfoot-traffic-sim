import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


public class Environment extends World {
    private boolean initialized = false;
    private ArrayList<WayPoint> wayPoints = new ArrayList<WayPoint>();

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
        drawStreet();
        markWayPoints();
        resolveStartWayPoints();
        connectWayPoints();
    }


    private void connectWayPoints() {
        for (WayPoint wayPoint : wayPoints) {
            if (wayPoint.type == WayPointType.START) {
                getBackground().setColor(Color.GREEN);
            } else if (wayPoint.type == WayPointType.END) {
                getBackground().setColor(Color.RED);
            } else if (wayPoint.type == WayPointType.INTERSECTION) {
                getBackground().setColor(Color.BLUE);
            }
            for (WayPoint wayPoint1 : wayPoint.wayPoints) {
                getBackground().drawLine(wayPoint.location.x, wayPoint.location.y, wayPoint1.location.x, wayPoint1.location.y);
                getBackground().setColor(Color.PINK);
                getBackground().fillRect(wayPoint1.location.x - 3, wayPoint1.location.y - 3, 6, 6);
            }

        }
    }

    private void markWayPoints() {
        for (WayPoint wayPoint : wayPoints) {
            if (wayPoint.type == WayPointType.START)
                getBackground().setColor(Color.GREEN);
            else if (wayPoint.type == WayPointType.END)
                getBackground().setColor(Color.RED);
            else if (wayPoint.type == WayPointType.INTERSECTION)
                getBackground().setColor(Color.BLUE);
            getBackground().fillRect(wayPoint.location.x - 5, wayPoint.location.y - 5, 10, 10);
        }
    }

    private void resolveStartWayPoints() {
        for (WayPoint wayPoint : wayPoints.stream().filter(wayPoint -> wayPoint.type == WayPointType.START).collect(Collectors.toList())) {
//            Forwards horizontal Resolution
            if (wayPoint.location.x <= getWidth() / 2)
                wayPoints.stream().filter(wayPoint1 -> wayPoint1.type != WayPointType.START && wayPoint1.location.x == wayPoint.location.x).min(Comparator.comparingInt(wayPoint2 -> wayPoint2.location.y)).ifPresent(point -> wayPoint.wayPoints.add(point));
//            Forwards vertical Resolution
            if (wayPoint.location.y > getHeight() - 150)
                wayPoints.stream().filter(wayPoint1 -> wayPoint1.type != WayPointType.START && wayPoint1.location.y == wayPoint.location.y).min(Comparator.comparingInt(wayPoint2 -> wayPoint2.location.x)).ifPresent(point -> wayPoint.wayPoints.add(point));
//            Backwards horizontal Resolution
            if (wayPoint.location.x > getWidth() / 2)
                wayPoints.stream().filter(wayPoint1 -> wayPoint1.type != WayPointType.START && wayPoint1.location.x == wayPoint.location.x).max(Comparator.comparingInt(wayPoint2 -> wayPoint2.location.y)).stream().findFirst().ifPresent(point -> wayPoint.wayPoints.add(point));
//            Backwards vertical Resolution
            if (wayPoint.location.y < 10)
                wayPoints.stream().filter(wayPoint1 -> wayPoint1.type != WayPointType.START && wayPoint1.location.y == wayPoint.location.y).max(Comparator.comparingInt(wayPoint2 -> wayPoint2.location.x)).stream().findFirst().ifPresent(point -> wayPoint.wayPoints.add(point));
        }
    }

    private void drawStreet() {
        int roadWidth = 50;
        GreenfootImage background = getBackground();
        background.setColor(Color.BLACK);
        Position oldPos = new Position(getWidth() / 2, getHeight());
        Position newPos = new Position(getWidth() / 2, getHeight());
        Position verticalPosA = new Position(0, 0);
        Position verticalPosB = new Position(0, 0);
//        background.drawLine(oldPos.x, oldPos.y, newPos.x, newPos.y);
        int margin = 100;
        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y), WayPointType.START));
        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y), WayPointType.END));
        while (newPos.y > 0) {
            margin--;
            if (newPos.y % (Greenfoot.getRandomNumber(600) + 1) == 0 && margin <= 0 && newPos.y > 100) {
                margin = 100;

                verticalPosA.y = newPos.y;
                verticalPosB.y = newPos.y;

                switch (Greenfoot.getRandomNumber(3)) {
//                    Left
                    case 0:
                        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y + (roadWidth / 4)), WayPointType.INTERSECTION));
                        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y - (roadWidth / 4)), WayPointType.INTERSECTION));
                        background.setColor(Color.BLACK);
                        verticalPosA.x = 0;
                        verticalPosB.x = newPos.x;
//                        Start and End WayPoints
                        wayPoints.add(new WayPoint(new Position(verticalPosA.x, newPos.y + (roadWidth / 4)), WayPointType.START));
                        wayPoints.add(new WayPoint(new Position(verticalPosA.x, newPos.y - (roadWidth / 4)), WayPointType.END));
                        break;
//                        Right
                    case 1:
                        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y - (roadWidth / 4)), WayPointType.INTERSECTION));
                        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y + (roadWidth / 4)), WayPointType.INTERSECTION));
                        background.setColor(Color.BLACK);
                        verticalPosA.x = newPos.x;
                        verticalPosB.x = getWidth();
//                        Start and End WayPoints
                        wayPoints.add(new WayPoint(new Position(verticalPosB.x, newPos.y - (roadWidth / 4)), WayPointType.START));
                        wayPoints.add(new WayPoint(new Position(verticalPosB.x, newPos.y + (roadWidth / 4)), WayPointType.END));
                        break;
//                        Straight
                    case 2:
                        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y + (roadWidth / 4)), WayPointType.INTERSECTION));
                        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y - (roadWidth / 4)), WayPointType.INTERSECTION));
                        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y + (roadWidth / 4)), WayPointType.INTERSECTION));
                        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y - (roadWidth / 4)), WayPointType.INTERSECTION));
                        background.setColor(Color.BLACK);
                        verticalPosA.x = 0;
                        verticalPosB.x = getWidth();
                        wayPoints.add(new WayPoint(new Position(verticalPosA.x, newPos.y + (roadWidth / 4)), WayPointType.START));
                        wayPoints.add(new WayPoint(new Position(verticalPosA.x, newPos.y - (roadWidth / 4)), WayPointType.END));
                        wayPoints.add(new WayPoint(new Position(verticalPosB.x, newPos.y - (roadWidth / 4)), WayPointType.START));
                        wayPoints.add(new WayPoint(new Position(verticalPosB.x, newPos.y + (roadWidth / 4)), WayPointType.END));
                        break;
                }
                background.fillRect(verticalPosA.x, verticalPosA.y - (roadWidth / 2), (verticalPosA.x - verticalPosB.x) * -1, roadWidth);
                background.setColor(Color.WHITE);
                background.drawLine(verticalPosA.x + (roadWidth / 2), verticalPosA.y, verticalPosB.x - (roadWidth / 2), verticalPosB.y);
//                Fill intersection like this:
                background.setColor(Color.BLACK);
//                background.fillRect(newPos.x - (roadWidth / 2), newPos.y - (roadWidth / 2), roadWidth, roadWidth);
                background.fillRect(newPos.x - (roadWidth / 2), newPos.y, roadWidth, Math.subtractExact(oldPos.y, newPos.y));
                background.setColor(Color.WHITE);
                background.drawLine(oldPos.x, oldPos.y - (roadWidth / 2), newPos.x, newPos.y + (roadWidth / 2));
                oldPos.y = newPos.y;
            }
            newPos.y--;
        }
        background.setColor(Color.BLACK);
        background.fillRect(newPos.x - (roadWidth / 2), newPos.y, roadWidth, Math.subtractExact(oldPos.y, newPos.y));
        background.setColor(Color.WHITE);
        background.drawLine(oldPos.x, oldPos.y - (roadWidth / 2), newPos.x, newPos.y);
        wayPoints.add(new WayPoint(new Position(newPos.x - (roadWidth / 4), newPos.y), WayPointType.START));
        wayPoints.add(new WayPoint(new Position(newPos.x + (roadWidth / 4), newPos.y), WayPointType.END));
    }

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
