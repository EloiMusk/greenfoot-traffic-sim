import greenfoot.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Environment extends World {
    public Graph roadNetwork = new Graph();
    private boolean initialized = false;
    private int loopCount = 0;

    private int carCount = 5;

    public Environment() {
        super(600, 600, 1, false);
        setBackground("textures/environment/1.jpg");
    }

    public void act() {
        if (!initialized) {
            init();
            initialized = true;
        }
        if (carCount > getObjects(Car.class).size()) {
            spawnCar();
        }
    }

    public void init() {
        generateGraph();
        markWayPoints();
        connectWayPoints();
    }

    public void spawnCar() {
//        int carCount = Greenfoot.getRandomNumber(this.roadNetwork.getAllIncomingEdgeNodes().length + 1);
        Car car = new Car();
//        Car[] cars = getObjects(Car.class).toArray(Car[]::new);
        WayPoint[] incomingWayPoints = getValidStartWayPoints();
        if (incomingWayPoints.length == 0) {
            return;
        }
        WayPoint startWayPoint = incomingWayPoints[Greenfoot.getRandomNumber(incomingWayPoints.length)];
        car.lastWayPoint = startWayPoint;
        addObject(car, startWayPoint.location.x, startWayPoint.location.y);
        Node[] edges = this.roadNetwork.getEdges(startWayPoint);
        Node nextNode = edges[Greenfoot.getRandomNumber(edges.length)];
        car.setNextNode(nextNode);

    }

    public WayPoint[] getValidStartWayPoints() {
        return Arrays.stream(this.roadNetwork.getAllIncomingEdgeNodes()).filter(wayPoint -> Arrays.stream(getObjectsInRange(Car.class, 100, wayPoint.location).toArray(Car[]::new)).noneMatch(searchCar -> searchCar.lastWayPoint == wayPoint)).toArray(WayPoint[]::new);
    }

    public <T extends Actor> List<T> getObjectsInRange(Class<T> cls, int range, int x, int y) {
        return getObjects(cls).stream().filter(obj -> new Vector(x, y, obj.getX(), obj.getY()).getDistance() <= range).collect(Collectors.toList());
    }

    public <T extends Actor> List<T> getObjectsInRange(Class<T> cls, int range, Position pos) {
        return getObjects(cls).stream().filter(obj -> new Vector(pos, new Position(obj.getX(), obj.getY())).getDistance() <= range).collect(Collectors.toList());
    }

    public Car[] getIntersectingTraffic(Car car) {
        return Arrays.stream(getObjects(Car.class).toArray(Car[]::new)).filter(otherCar -> otherCar.nextNode == car.nextNode).toArray(Car[]::new);
    }

    public void removeCar(Car car) {
        removeObject(car);
        spawnCar();
    }

    public Node getRandomNextNode(Node node) {
        Node[] edges = Arrays.stream(this.roadNetwork.getEdges(node.value)).filter(edge -> edge.direction != node.direction.reverseDirection()).toArray(Node[]::new);
        if (edges.length == 0) {
            return null;
        } else {
            return edges[Greenfoot.getRandomNumber(edges.length)];
        }
    }

    private void generateGraph() {
        Position oldPos = new Position(getWidth() / 2, getHeight());
        Position newPos = new Position(getWidth() / 2, getHeight());
        GreenfootImage background = getBackground();
        int roadWidth = 50;
//        background.drawLine(oldPos.x, oldPos.y, newPos.x, newPos.y);
        WayPoint oldWayPoint = new WayPoint(new Position(oldPos.x, oldPos.y));
        WayPoint newWayPoint = new WayPoint(new Position(newPos.x, newPos.y));
        int margin = 150;
        while (newPos.y > 0) {
            margin--;
            if (newPos.y % (Greenfoot.getRandomNumber(600) + 1) == 0 && margin <= 0 && newPos.y > 100) {
                margin = 150;

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
    }


    private void connectWayPoints() {
        int roadWidth = 50;
        WayPoint[] wayPoints = roadNetwork.getVertices();
        for (WayPoint wayPoint : wayPoints) {
            for (Node edge : roadNetwork.getEdges(wayPoint)) {
                int y;
                int x;
                getBackground().setColor(Color.BLACK);
                switch (edge.direction) {
                    case NORTH:
                        showText("N: " + edge.speedLimit + " km/h", edge.value.location.x, edge.value.location.y - 10);
                        getBackground().fillRect(edge.value.location.x, edge.value.location.y, roadWidth, new Vector(wayPoint.location, edge.value.location).getDistance());
                        break;
                    case EAST:
                        if (edge.value.location.x < wayPoint.location.x) {
                            y = edge.value.location.y;
                            x = edge.value.location.x;
                            showText("E: " + edge.speedLimit + " km/h", x - 50, y + 30);
                        } else {
                            y = wayPoint.location.y;
                            x = wayPoint.location.x;
                            showText("E: " + edge.speedLimit + " km/h", x + 50, y + 30);
                        }
                        getBackground().fillRect(x, y, new Vector(wayPoint.location, edge.value.location).getDistance(), roadWidth);
                        break;
                    case SOUTH:
                        showText("S: " + edge.speedLimit + " km/h", edge.value.location.x, edge.value.location.y + 10);
                        getBackground().fillRect(wayPoint.location.x - roadWidth, wayPoint.location.y, roadWidth, new Vector(wayPoint.location, edge.value.location).getDistance());
                        break;
                    case WEST:
                        if (edge.value.location.x < wayPoint.location.x) {
                            y = edge.value.location.y;
                            x = edge.value.location.x;
                            showText("W: " + edge.speedLimit + " km/h", x + 50, y - 30);
                        } else {
                            y = wayPoint.location.y;
                            x = wayPoint.location.x;
                            showText("W: " + edge.speedLimit + " km/h", x - 50, y - 30);
                        }
                        getBackground().fillRect(x, y - roadWidth, new Vector(wayPoint.location, edge.value.location).getDistance(), roadWidth);
                        break;
                }
            }
        }
    }

    private void markWayPoints() {
        WayPoint[] wayPoints = roadNetwork.getVertices();
//        System.out.println("WayPoints: " + wayPoints.length);
        for (WayPoint wayPoint : wayPoints) {
            getBackground().setColor(Color.WHITE);
            getBackground().fillRect(wayPoint.location.x - 5, wayPoint.location.y - 5, 10, 10);
        }
    }

}
