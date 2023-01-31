import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Greenfoot;

import java.util.Arrays;

public class Car extends Actor {

    public Node nextNode;
    public WayPoint lastWayPoint;

    private int speed;

    private int acceleration = 5;

    private int deceleration = 5;

    private DriveState driveState = DriveState.CRUISING;

    public Direction orientation;

    public Car() {
        setImage("cars/" + (Greenfoot.getRandomNumber(3) + 1) + ".png");
        int newHeight = (int) (getImage().getHeight() * (50.0 / getImage().getWidth()));
        getImage().scale(50, newHeight);
    }

    public void act() {
        checkSurroundings();
        checkSpeed();
        checkDistanceToNextCar();
        move(speed);
        checkPosition();
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

    private void checkPosition() {
        if (isAtEdge()) {
            this.destroy();
        } else if (new Vector(getX(), getY(), nextNode.value.location.x, nextNode.value.location.y).getDistance() <= 25) {
            Environment world = (Environment) getWorld();
            setNextNode(world.getRandomNextNode(nextNode));
        }
    }

    private void checkSurroundings() {
        checkDistanceToNextCar();
    }

    private void checkDistanceToNextCar() {
        Car nextCar = getNextCar();
        if (nextCar != null) {
            int distance = new Vector(getX(), getY(), nextCar.getX(), nextCar.getY()).getDistance();
            if (distance < 150) {
                decelerate();
            } else if (distance > 200) {
                accelerateToSpeedLimit();
            }
        } else {
            accelerateToSpeedLimit();
        }

    }

    private boolean isNextCarInFront(Car nextCar) {
        if (nextCar != null) {
            int distance = new Vector(getX(), getY(), nextCar.getX(), nextCar.getY()).getDistance();
            return distance < 50;
        }
        return false;
    }

    private Car getNextCar() {
        return Arrays.stream(getObjectsInRange(200, Car.class).toArray(new Car[0])).filter(this::isNextCarInFront).sorted((car1, car2) -> {
            switch (this.orientation) {
                case NORTH:
                    return car1.getY() - car2.getY();
                case EAST:
                    return car2.getX() - car1.getX();
                case SOUTH:
                    return car2.getY() - car1.getY();
                case WEST:
                    return car1.getX() - car2.getX();
            }
            return 0;
        }).findFirst().orElse(null);
    }

    private void checkSpeed() {
        int speedLimit = nextNode.speedLimit;
        if (speed > speedLimit) {
            decelerate();
        } else if (speed < speedLimit) {
            accelerate();
        } else {
            driveState = DriveState.CRUISING;
        }
    }

    private void accelerate() {
        deceleration = 5;
        speed += acceleration;
        driveState = DriveState.ACCELERATING;
    }

    private void accelerateToSpeedLimit() {
        if (speed < nextNode.speedLimit) {
            accelerate();
            driveState = DriveState.ACCELERATING;
        } else {
            driveState = DriveState.CRUISING;
        }
    }

    private void decelerate() {
        if (speed > 0) {
            deceleration = deceleration * 2;
            speed -= deceleration;
            driveState = DriveState.DECELERATING;
        } else {
            deceleration = 5;
            speed = 0;
            driveState = DriveState.STOPPED;
        }
    }

    @Override
    public boolean isAtEdge() {
        return (getX() <= -getImage().getWidth() || getX() >= getWorld().getWidth() + getImage().getWidth() || getY() <= -getImage().getHeight() || getY() >= getWorld().getHeight() + getImage().getHeight());
    }

    @Override
    public void move(int speed) {
        int distance = speed / 20;
        super.move(distance);
    }

}
