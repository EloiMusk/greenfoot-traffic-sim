public class Edge {
    int weight;
    WayPoint source;
    WayPoint destination;
    Direction direction;

    //creating a constructor of the class Edge
    Edge(WayPoint source, WayPoint destination, Direction direction) {
        this.source = source;
        this.destination = destination;
        this.weight = (int) (100 / Math.sqrt(Math.pow(600, 2) + Math.pow(600, 2)) * new Vector(source.location, destination.location).getDistance());
        this.direction = direction;
    }
}