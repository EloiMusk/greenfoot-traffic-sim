import greenfoot.World;

import java.util.Random;

public class Environment extends World {
    private boolean initialized = false;

    public Environment() {
        super(600, 600, 1, false);
        setBackground("textures/environment/1.jpg");
    }

    public void act() {
        if (!initialized) {
            init();
            initialized = true;
        }
    }

    public void init() {
        generateLanes();
    }


    public void generateLanes() {
        Random rand = new Random();
        int numLanes = rand.nextInt(5) + 1; // Generate a random number of lanes between 1 and 5
        int laneWidth = 100; // Set the width of each lane
        int laneLength = getHeight() / numLanes; // Calculate the length of each lane based on the number of lanes and the height of the world

        // Generate a random starting position and direction for the first lane, making sure that the starting position is on one of the borders of the Environment
        int x, y, direction;
        if (rand.nextBoolean()) {
            // Start the lane on the top or bottom border
            x = rand.nextInt(getWidth());
            if (rand.nextBoolean()) {
                // Start the lane on the top border
                y = 0;
                direction = 90;
            } else {
                // Start the lane on the bottom border
                y = getHeight() - laneWidth;
                direction = 270;
            }
        } else {
            // Start the lane on the left or right border
            y = rand.nextInt(getHeight());
            if (rand.nextBoolean()) {
                // Start the lane on the left border
                x = 0;
                direction = 0;
            } else {
                // Start the lane on the right border
                x = getWidth() - laneWidth;
                direction = 180;
            }

            // Calculate the ending position of the first lane based on the starting position, direction, and length, making sure that the ending position is also on one of the borders of the Environment
            int x2 = x + (int) (Math.cos(Math.toRadians(direction)) * laneLength);
            int y2 = y + (int) (Math.sin(Math.toRadians(direction)) * laneLength);
            if (x2 < 0 || x2 >= getWidth()) {
                // The ending position is on the left or right border
                x2 = (x2 < 0) ? 0 : getWidth() - laneWidth;
                y2 = y;
            } else if (y2 < 0 || y2 >= getHeight()) {
                // The ending position is on the top or bottom border
                x2 = x;
                y2 = (y2 < 0) ? 0 : getHeight() - laneWidth;
            }

            // Create the first lane with the calculated starting and ending positions
            Lane lane = new Lane(x, y, x2, y2);

            // Add the first lane to the world
            addObject(lane, 0, 0);
            // Generate the remaining lanes
            for (int i = 1; i < numLanes; i++) {
                // Use the ending position of the previous lane as the starting position for the next lane
                x = x2;
                y = y2;

                // Generate a random direction for the next lane, either parallel or perpendicular to the previous lane
                if (rand.nextBoolean()) {
                    // Keep the same direction as the previous lane
                    direction = lane.vector.getDirection();
                } else {
                    // Change the direction to be perpendicular to the previous lane
                    direction = lane.vector.getDirection() + 90;
                    if (direction >= 360) {
                        direction -= 360;
                    }
                }

                // Calculate the ending position of the next lane based on the starting position, direction, and length, making sure that the ending position is also on one of the borders of the Environment
                x2 = x + (int) (Math.cos(Math.toRadians(direction)) * laneLength);
                y2 = y + (int) (Math.sin(Math.toRadians(direction)) * laneLength);
                if (x2 < 0 || x2 >= getWidth()) {
                    // The ending position is on the left or right border
                    x2 = (x2 < 0) ? 0 : getWidth() - laneWidth;
                    y2 = y;
                } else if (y2 < 0 || y2 >= getHeight()) {
                    // The ending position is on the top or bottom border
                    x2 = x;
                    y2 = (y2 < 0) ? 0 : getHeight() - laneWidth;
                }

                // Create the next lane with the calculated starting and ending positions
                lane = new Lane(x, y, x2, y2);

                // Add the next lane to the world
                addObject(lane, 0, 0);
            }
        }
    }
}
