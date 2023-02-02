import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a graph. It contains a map that stores the edges of the graph.
 *
 * @author EloiMusk
 * @version 1.0
 */
class Graph {

    /**
     * This map stores the edges of the graph. The key is the source vertex and the value is a list of nodes that represent
     * the edges of the graph.
     */
    private Map<WayPoint, List<Node>> map = new HashMap<>();

    /**
     * This is the current way point of the graph.
     */
    private WayPoint currentWayPoint;

    /**
     * This method returns the current way point of the graph.
     *
     * @return the current way point of the graph.
     */
    public WayPoint getCurrentWayPoint() {
        return currentWayPoint;
    }

    /**
     * This method sets the current way point of the graph.
     *
     * @param currentWayPoint the current way point of the graph.
     */
    public void setCurrentWayPoint(WayPoint currentWayPoint) {
        this.currentWayPoint = currentWayPoint;
    }

    /**
     * This method returns all incoming edge nodes of the graph.
     *
     * @return all incoming edge nodes of the graph.
     */
    public WayPoint[] getAllIncomingEdgeNodes() {
        // All nodes that have only one outgoing edge
        return map.keySet().stream().filter(wayPoint -> map.get(wayPoint).size() == 1).toArray(WayPoint[]::new);
    }

    /**
     * This method gets all edges of a WayPoint.
     *
     * @param source the source WayPoint.
     * @return all edges of a WayPoint.
     */
    public Node[] getEdges(WayPoint source) {
        return map.get(source).toArray(Node[]::new);
    }

    /**
     * This method gets the vertex of a position.
     *
     * @param position the position of the vertex.
     * @return the vertex of a position or null if the vertex does not exist.
     */
    public WayPoint getVertex(Position position) {
        return map.keySet().stream().filter(wayPoint -> wayPoint.location.equals(position)).findFirst().orElse(null);
    }

    /**
     * This method gets all vertices of the graph.
     *
     * @return all vertices of the graph.
     */
    public WayPoint[] getVertices() {
        return map.keySet().toArray(new WayPoint[0]);
    }

    /**
     * This method adds a new vertex to the graph.
     *
     * @param source the way point to add.
     */
    public void addNewVertex(WayPoint source) {
        map.put(source, new LinkedList<Node>());
    }

    /**
     * The method adds an edge between source and destination.
     *
     * @param edge          the edge to add.
     * @param bidirectional true if the edge is bidirectional, false otherwise.
     */
    public void addNewEdge(Edge edge, boolean bidirectional) {
        if (!map.containsKey(edge.source))
            addNewVertex(edge.source);
        if (!map.containsKey(edge.destination))
            addNewVertex(edge.destination);
        map.get(edge.source).add(new Node(edge.destination, edge.weight, edge.direction));
        if (bidirectional) {
            map.get(edge.destination).add(new Node(edge.source, edge.weight, edge.direction.reverseDirection()));
        }
    }

    /**
     * This method counts the number of vertices.
     */
    public void countVertices() {
        System.out.println("Total number of vertices: " + map.keySet().size());
    }

    /**
     * This method counts the number of edges.
     *
     * @param bidirectional true if bidirectional edges are counted, false otherwise.
     */
    public void countEdges(boolean bidirectional) {
        //variable to store number of edges
        int count = 0;
        for (WayPoint v : map.keySet()) {
            count = count + map.get(v).size();
        }
        if (bidirectional) {
            count = count / 2;
        }
        System.out.println("Total number of edges: " + count);
    }

    /**
     * This method checks if the graph contains a vertex.
     *
     * @param s the vertex to check.
     */
    public void containsVertex(WayPoint s) {
        if (map.containsKey(s)) {
            System.out.println("The graph contains " + s + " as a vertex.");
        } else {
            System.out.println("The graph does not contain " + s + " as a vertex.");
        }
    }

    /**
     * This method checks if the graph contains an edge.
     *
     * @param s the source vertex.
     * @param d the destination vertex.
     */
    public void containsEdge(WayPoint s, WayPoint d) {
        if (map.get(s).stream().anyMatch(node -> node.value == d)) {
            System.out.println("The graph has an edge between " + s + " and " + d + ".");
        } else {
            System.out.println("There is no edge between " + s + " and " + d + ".");
        }
    }

    /**
     * Overridden toString method of the StringBuilder class.
     *
     * @return the string representation of the graph.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        //foreach loop that iterates over the keys
        for (WayPoint v : map.keySet()) {
            builder.append(v.hashCode() + " --> " + v.toString() + ": ");
            //foreach loop for getting the vertices
            for (Node w : map.get(v)) {
                builder.append(w.toString() + " ");
            }
            builder.append("\n");
        }
        return (builder.toString());
    }
}