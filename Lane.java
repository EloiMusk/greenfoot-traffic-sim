import greenfoot.Actor;
import greenfoot.Color;

import java.util.ArrayList;

public class Lane extends Actor {
    public Vector vector;
    public ArrayList<Position> intersections = new ArrayList<Position>();
    private boolean initialized = false;

    public Lane(Position a, Position b) {
        vector = new Vector(a, b);
    }

    public Lane(int x1, int y1, int x2, int y2) {
        vector = new Vector(new Position(x1, y1), new Position(x2, y2));
    }

    public void act() {
        if (!initialized) {
            init();
            initialized = true;
        }
    }

    private void init() {
        // Scale the actor according to the length of the vector.
        int length = vector.getDistance();
        setImage("textures/lane/1.jpg");
        getImage().scale(50, length);
        for (Position intersection : intersections) {
            getImage().setColor(Color.WHITE);
            getImage().drawLine(25, 0, 25, intersection.y);
        }


        // Place the actor in the right location.
        int x = vector.a.x + (vector.b.x - vector.a.x) / 2;
        int y = vector.a.y + (vector.b.y - vector.a.y) / 2;
        this.setLocation(x, y);

        // Set the rotation of the actor so that it points in the direction of the vector.
        int rotation = vector.getDirection();

        setRotation(rotation - 90);

//        // Draw the image repeatedly along the length of the actor.
//        GreenfootImage image = getImage();
//        int imageWidth = image.getWidth();
//        int imageHeight = image.getHeight();
//        for (int i = 0; i < length; i += imageHeight) {
//            image.drawImage(image, i, 0);
//        }
//        System.out.println("Width: " + image.getWidth());
//        System.out.println("Height: " + image.getHeight());
//        System.out.println("Position: " + this.getX() + ", " + this.getY());
    }

}
