import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Greenfoot;

public class Car extends Actor {

    private Node nextNode;

    public WayPoint lastWayPoint;

    private Direction orientation;

    public Car() {
        setImage("cars/" + (Greenfoot.getRandomNumber(3) + 1) + ".png");
        int newHeight = (int) (getImage().getHeight() * (50.0 / getImage().getWidth()));
        getImage().scale(50, newHeight);
    }

    public void setNextNode(Node node) {
        if (node == null) {
            this.destroy();
        } else {
            this.nextNode = node;
            setOrientation(this.nextNode.direction);
        }
    }

    public void setOrientation(Direction direction) {
        this.orientation = direction;
        switch (direction) {
            case NORTH:
                setLocation(this.nextNode.value.location.x + 25, getY());
                setRotation(270);
                break;
            case EAST:
                setLocation(getX(), this.nextNode.value.location.y + 25);
                setRotation(0);
                break;
            case SOUTH:
                setLocation(this.nextNode.value.location.x - 25, getY());
                setRotation(90);
                break;
            case WEST:
                setLocation(getX(), this.nextNode.value.location.y - 25);
                setRotation(180);
                break;
        }
    }

    private void destroy() {
        Environment world = (Environment) getWorld();
        world.removeCar(this);
    }

    @Override
    public boolean isAtEdge() {
        return (getX() <= getImage().getWidth() || getX() >= getWorld().getWidth() + getImage().getWidth() || getY() <= -getImage().getHeight() || getY() >= getWorld().getHeight() + getImage().getHeight());
    }

    private void checkPosition() {
        if (isAtEdge()) {
            this.destroy();
        } else if (new Vector(getX(), getY(), nextNode.value.location.x, nextNode.value.location.y).getDistance() <= 25) {
            Environment world = (Environment) getWorld();
            setNextNode(world.getRandomNextNode(nextNode));
        }
    }

    public void act() {
        move(1);
        checkPosition();
    }


}
