/**
 * This class represents an edge in the graph. It contains the source, destination, weight and direction of the edge.
 * @author EloiMusk
 */
public class Edge {
    int weight;
    WayPoint source;
    WayPoint destination;
    Direction direction;

    /**
     * Creates a new edge.
     * @param source The source of the edge.
     * @param destination The destination of the edge.
     * @param direction The direction of the edge.
     */
    Edge(WayPoint source, WayPoint destination, Direction direction) {
        this.source = source;
        this.destination = destination;
        this.weight = (int) (100 / Math.sqrt(Math.pow(600, 2) + Math.pow(600, 2)) * new Vector(source.location, destination.location).getDistance());
        this.direction = direction;
    }
}