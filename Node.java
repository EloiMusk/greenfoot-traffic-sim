import java.util.Random;

/**
 * This class represents a node. It contains a weight, a value, a direction and a speed limit.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Node {
    /**
     * The weight of the node.
     */
    int weight; // distance as weight

    /**
     * The value of the node as a WayPoint.
     */
    WayPoint value;

    /**
     * The direction of the node relative to the source node.
     */
    Direction direction;

    /**
     * The speed limit of the node.
     */
    int speedLimit;

    /**
     * This is the constructor of the class Node.
     *
     * @param value     the value of the node as a WayPoint.
     * @param weight    the weight of the node.
     * @param direction the direction of the node relative to the source node.
     */
    Node(WayPoint value, int weight, Direction direction) {
        this.value = value;
        this.weight = weight;
        this.direction = direction;
        this.speedLimit = new Random().nextInt(50) + 50;
    }

    /**
     * This method returns a string representation of the node.
     *
     * @return a string representation of the node.
     */
    @Override
    public String toString() {
        return this.value.toString() + " (" + this.weight + ")";
    }
}
