import greenfoot.Actor;
import greenfoot.Greenfoot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Car extends Actor {

    public Node finalNode;
    public Node nextNode;
    public WayPoint lastWayPoint;

    private int speed;

    private int acceleration = 5;

    private int deceleration = 5;
    private boolean isTurning = false;

    private DriveState driveState = DriveState.CRUISING;

    public Direction orientation;

    public Map<Direction, Boolean> rightOfWay = new HashMap<Direction, Boolean>();

    public Car() {
        setImage("cars/" + (Greenfoot.getRandomNumber(3) + 1) + ".png");
        int newHeight = (int) (getImage().getHeight() * (50.0 / getImage().getWidth()));
        getImage().scale(50, newHeight);
    }

    public void act() {
        checkSpeed();
        checkDistanceToNextCar();
        move(speed);
        checkPosition();
        checkCollision();
    }

    private void checkCollision() {
        try {
            if (isTouching(Car.class)) {
                this.destroy();
            }
        }catch (Exception e) {
            System.out.println("Car crashed");
        }

    }

    public void setNextNode(Node node) {
        if (node == null) {
            this.destroy();
        } else {
            this.nextNode = node;
            Environment world = (Environment) getWorld();
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
        } else if (new Vector(getX(), getY(), nextNode.value.location.x, nextNode.value.location.y).getDistance() <= 50 && !isTurning) {
            Environment world = (Environment) getWorld();
            startTurning(world.getRandomNextNode(nextNode));
        }
    }

    private void startTurning(Node nextNode) {
        if (nextNode == null) {
            return;
        }
        this.isTurning = true;
        this.nextNode = nextNode;
        this.lastWayPoint = nextNode.value;
        turnInDirection(nextNode.direction);
    }

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
            deceleration = 10;
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
        if (isTurning) {
            turnInDirection(nextNode.direction);
            super.move(speed / 15);
        } else {
            int distance = speed / 20;
            super.move(distance);
        }
    }

}
