import greenfoot.Actor;
import greenfoot.Color;

public class Car extends Actor {

    private Node nextNode;

    public Car() {
        getImage().setColor(Color.RED);
        getImage().fillRect(0, 0, 50, 50);
    }

    public void setNextNode(Node node) {
        this.nextNode = node;
        setOrientation(this.nextNode.direction);
    }

    public void setOrientation(Direction direction) {
        switch (direction) {
            case NORTH:
                setLocation(getX() + 25, getY());
                setRotation(270);
                break;
            case EAST:
                setLocation(getX(), getY() + 25);
                setRotation(0);
                break;
            case SOUTH:
                setLocation(getX() - 25, getY());
                setRotation(90);
                break;
            case WEST:
                setLocation(getX(), getY() - 25);
                setRotation(180);
                break;
        }
    }

    public void act() {
        move(1);
    }


}
