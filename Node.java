public class Node {
    int distance; // distance as weight
    WayPoint value;
    Direction direction;

    //creating a constructor of the class Vertex
    Node(WayPoint value, int distance, Direction direction) {
        this.value = value;
        this.distance = distance;
        this.direction = direction;
    }

    //overrides the toString() method
    @Override
    public String toString() {
        return this.value.toString() + " (" + this.distance + ")";
    }
}
