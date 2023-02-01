import java.util.Random;

public class Node {
    int weight; // distance as weight
    WayPoint value;
    Direction direction;

    int speedLimit;

    //creating a constructor of the class Vertex
    Node(WayPoint value, int weight, Direction direction) {
        this.value = value;
        this.weight = weight;
        this.direction = direction;
        this.speedLimit = new Random().nextInt(50) + 50;
    }

    //overrides the toString() method
    @Override
    public String toString() {
        return this.value.toString() + " (" + this.weight + ")";
    }
}
