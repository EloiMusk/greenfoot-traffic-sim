import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.Arrays;

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
        generateGraph();
        markWayPoints();
        connectWayPoints();
        spawnCar();
    }

    public void spawnCar() {
        Car car = new Car();
        WayPoint[] incomingWayPoints = this.roadNetwork.getAllIncomingEdgeNodes();
        WayPoint startWayPoint = incomingWayPoints[Greenfoot.getRandomNumber(incomingWayPoints.length)];
        addObject(car, startWayPoint.location.x, startWayPoint.location.y);
        Node[] edges = this.roadNetwork.getEdges(startWayPoint);
        Node nextNode = edges[Greenfoot.getRandomNumber(edges.length)];
        car.setNextNode(nextNode);
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
//        System.out.println(roadNetwork.toString());
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
                        getBackground().fillRect(edge.value.location.x, edge.value.location.y, roadWidth, new Vector(wayPoint.location, edge.value.location).getDistance());
                        break;
                    case EAST:
                        if (edge.value.location.x < wayPoint.location.x) {
                            y = edge.value.location.y;
                            x = edge.value.location.x;
                        } else {
                            y = wayPoint.location.y;
                            x = wayPoint.location.x;
                        }
                        getBackground().fillRect(x, y, new Vector(wayPoint.location, edge.value.location).getDistance(), roadWidth);
                        break;
                    case SOUTH:
                        getBackground().fillRect(wayPoint.location.x - roadWidth, wayPoint.location.y, roadWidth, new Vector(wayPoint.location, edge.value.location).getDistance());
                        break;
                    case WEST:
                        if (edge.value.location.x < wayPoint.location.x) {
                            y = edge.value.location.y;
                            x = edge.value.location.x;
                        } else {
                            y = wayPoint.location.y;
                            x = wayPoint.location.x;
                        }
                        getBackground().fillRect(x, y - roadWidth, new Vector(wayPoint.location, edge.value.location).getDistance(), roadWidth);
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

}
