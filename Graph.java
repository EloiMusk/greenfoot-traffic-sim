import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Graph {
    //creating an object of the Map class that stores the edges of the graph
    private Map<WayPoint, List<Node>> map = new HashMap<>();

    private WayPoint currentWayPoint;

    public WayPoint getCurrentWayPoint() {
        return currentWayPoint;
    }

    public void setCurrentWayPoint(WayPoint currentWayPoint) {
        this.currentWayPoint = currentWayPoint;
    }

    public WayPoint[] getAllIncomingEdgeNodes() {
        // All nodes that have only one outgoing edge
        return map.keySet().stream().filter(wayPoint -> map.get(wayPoint).size() == 1).toArray(WayPoint[]::new);
    }

    public Node[] getEdges(WayPoint source) {
        return map.get(source).toArray(Node[]::new);
    }

    public WayPoint getVertex(Position position) {
        return map.keySet().stream().filter(wayPoint -> wayPoint.location.equals(position)).findFirst().orElse(null);
    }

    public WayPoint[] getVertices() {
        return map.keySet().toArray(new WayPoint[0]);
    }

    //the method adds a new vertex to the graph
    public void addNewVertex(WayPoint source) {
        map.put(source, new LinkedList<Node>());
    }

    //the method adds an edge between source and destination
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

    //the method counts the number of vertices
    public void countVertices() {
        System.out.println("Total number of vertices: " + map.keySet().size());
    }

    //the method counts the number of edges
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

    //checks a graph has vertex or not
    public void containsVertex(WayPoint s) {
        if (map.containsKey(s)) {
            System.out.println("The graph contains " + s + " as a vertex.");
        } else {
            System.out.println("The graph does not contain " + s + " as a vertex.");
        }
    }

    //checks a graph has edge or not
//where s and d are the two parameters that represent source(vertex) and destination (vertex)  
    public void containsEdge(WayPoint s, WayPoint d) {
        if (map.get(s).stream().anyMatch(node -> node.value == d)) {
            System.out.println("The graph has an edge between " + s + " and " + d + ".");
        } else {
            System.out.println("There is no edge between " + s + " and " + d + ".");
        }
    }

    //prints the adjacencyS list of each vertex
//here we have overridden the toString() method of the StringBuilder class  
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