public class Node {
    int weight; // distance as weight
    WayPoint value;
    Direction direction;

    //creating a constructor of the class Vertex
    Node(WayPoint value, int weight, Direction direction) {
        this.value = value;
        this.weight = weight;
        this.direction = direction;
    }

    //overrides the toString() method
    @Override
    public String toString() {
        return this.value.toString() + " (" + this.weight + ")";
    }
}
