import greenfoot.Actor;
import greenfoot.Greenfoot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class of the car object.
 * This class is responsible for the controls of the car.
 * It also contains the logic for the car to drive.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Car extends Actor {

    /**
     * The next node the car is going to
     */
    public Node nextNode;
    /**
     * The last node the car was at
     */
    public WayPoint lastWayPoint;
    /**
     * The speed of the car
     */
    private int speed;

    /**
     * The acceleration of the car
     */
    private int acceleration = 5;

    /**
     * The deceleration of the car
     */
    private int deceleration = 5;

    /**
     * If the car is turning
     */
    private boolean isTurning = false;

    /**
     * The current state of the car
     */
    private DriveState driveState = DriveState.CRUISING;

    /**
     * The current orientation of the car
     */
    public Direction orientation;

    /**
     * A Map of the right of way of the car for each direction
     */
    public Map<Direction, Boolean> rightOfWay = new HashMap<Direction, Boolean>();

    /**
     * Constructor of the car object
     */
    public Car() {
        setImage("cars/" + (Greenfoot.getRandomNumber(3) + 1) + ".png");
        int newHeight = (int) (getImage().getHeight() * (50.0 / getImage().getWidth()));
        getImage().scale(50, newHeight);
    }

    /**
     * act method of the Actor class
     */
    public void act() {
        checkSpeed();
        checkDistanceToNextCar();
        move(speed);
        checkPosition();
        checkCollision();
    }

    /**
     * Checks if the car has crashed
     */
    private void checkCollision() {
        try {
            if (isTouching(Car.class)) {
                this.destroy();
            }
        } catch (Exception e) {
            System.out.println("Car crashed");
        }

    }

    /**
     * Sets the next node of the car
     * @param node
     */
    public void setNextNode(Node node) {
        if (node == null) {
            this.destroy();
        } else {
            this.nextNode = node;
            Environment world = (Environment) getWorld();
            setOrientation(this.nextNode.direction);
        }
    }

    /**
     * sets the orientation of the car
     * @param direction
     */
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

    /**
     * destroys the instance of the car
     */
    private void destroy() {
        Environment world = (Environment) getWorld();
        world.removeCar(this);
    }

    /**
     * Checks the distance to the next node and if the car is at the edge of the world
     */
    private void checkPosition() {
        if (isAtEdge()) {
            this.destroy();
        } else if (new Vector(getX(), getY(), nextNode.value.location.x, nextNode.value.location.y).getDistance() <= 50 && !isTurning) {
            Environment world = (Environment) getWorld();
            startTurning(world.getRandomNextNode(nextNode));
        }
    }

    /**
     * Starts a turn towards the next node
     * @param nextNode
     */
    private void startTurning(Node nextNode) {
        if (nextNode == null) {
            return;
        }
        this.isTurning = true;
        this.nextNode = nextNode;
        this.lastWayPoint = nextNode.value;
        turnInDirection(nextNode.direction);
    }

    /**
     * turns the car towards a direction
     * @param direction
     */
    private void turnInDirection(Direction direction) {
        switch (direction) {
            case NORTH:
                if (getRotation() < 270) {
                    turn(10);
                } else if (getRotation() > 270) {
                    turn(-10);
                }
                if (getRotation() > 260 && getRotation() < 280) {
                    this.isTurning = false;
                    setOrientation(direction);
                }
                break;
            case EAST:
                if (getRotation() < 0) {
                    turn(10);
                } else if (getRotation() > 0) {
                    turn(-10);
                }
                if (getRotation() > -10 && getRotation() < 10) {
                    this.isTurning = false;
                    setOrientation(direction);
                }
                break;
            case SOUTH:
                if (getRotation() < 90) {
                    turn(10);
                } else if (getRotation() > 90) {
                    turn(-10);
                }
                if (getRotation() > 80 && getRotation() < 100) {
                    this.isTurning = false;
                    setOrientation(direction);
                }
                break;
            case WEST:
                if (getRotation() < 180) {
                    turn(10);
                } else if (getRotation() > 180) {
                    turn(-10);
                }
                if (getRotation() > 170 && getRotation() < 190) {
                    this.isTurning = false;
                    setOrientation(direction);
                }
                break;
        }
    }

    /**
     * Checks the distance to the next car
     */
    private void checkDistanceToNextCar() {
        Car nextCar = getNextCar();
        if (nextCar != null) {
            Direction trajectoryToNextCar = new Vector(getX(), getY(), nextCar.getX(), nextCar.getY()).getOrientation();
            int distance = new Vector(getX(), getY(), nextCar.getX(), nextCar.getY()).getDistance();
            if (distance < 200 && trajectoryToNextCar == orientation) {
                decelerate();
            } else if (distance > 200) {
                accelerateToSpeedLimit();
            }
        } else {
            accelerateToSpeedLimit();
        }

    }

    /**
     * Checks if the car from the parameter is in front of the car
     * @param nextCar
     * @return true if the car is closer than 50 pixels
     */
    private boolean isNextCarInFront(Car nextCar) {
        if (nextCar != null) {
            int distance = new Vector(getX(), getY(), nextCar.getX(), nextCar.getY()).getDistance();
            return distance < 50;
        }
        return false;
    }

    /**
     * Gets the next car in front of the car
     * @return the next car in front of the car
     */
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

    /**
     * Checks the speed of the car
     */
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

    /**
     * Accelerates the car
     */
    private void accelerate() {
        deceleration = 5;
        speed += acceleration;
        driveState = DriveState.ACCELERATING;
    }

    /**
     * Accelerates the car to the speed limit
     */
    private void accelerateToSpeedLimit() {
        if (speed < nextNode.speedLimit) {
            accelerate();
            driveState = DriveState.ACCELERATING;
        } else {
            driveState = DriveState.CRUISING;
        }
    }

    /**
     * Decelerates the car
     */
    private void decelerate() {
        if (speed > 0) {
            deceleration = deceleration * 2;
            speed -= deceleration;
            driveState = DriveState.DECELERATING;
        } else {
            deceleration = 10;
            speed = 0;
            driveState = DriveState.STOPPED;
        }
    }

    /**
     * Overrides the isAtEdge method to check if the car is at the edge of the world respecting the bounds of the car
     * @return true if the car is at the edge of the world
     */
    @Override
    public boolean isAtEdge() {
        return (getX() <= -getImage().getWidth() || getX() >= getWorld().getWidth() + getImage().getWidth() || getY() <= -getImage().getHeight() || getY() >= getWorld().getHeight() + getImage().getHeight());
    }

    /**
     * Overrides the move method to move the car to work with the speed of the car
     * @param speed
     */
    @Override
    public void move(int speed) {
        if (isTurning) {
            turnInDirection(nextNode.direction);
            super.move(speed / 15);
        } else {
            int distance = speed / 20;
            super.move(distance);
        }
    }

}
