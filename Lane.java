import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;

public class Lane extends Actor {
    public Vector vector;
    private boolean initialized = false;
    public Lane(Position a, Position b) {
        vector = new Vector(a, b);
    }
    public Lane(int x1, int y1, int x2, int y2) {
        vector = new Vector(new Position(x1, y1), new Position(x2, y2));
    }

    public void act(){
        if(!initialized) {
            init();
            initialized = true;
        }
    }

    private void init() {
        // Scale the actor according to the length of the vector.
        int length = vector.getLength();
        System.out.println("Length: " + length);
        setImage("textures/lane/1.jpg");
        getImage().scale(50, length);

        // Place the actor in the right location.
        int x = vector.a.x;
        int y = vector.a.y - getImage().getHeight() / 2;
        this.setLocation(x, y);

        // Set the rotation of the actor so that it points in the direction of the vector.
        int rotation = vector.getDirection();
        System.out.println("Rotation: " + rotation);
        setRotation(rotation - 90);

        // Draw the image repeatedly along the length of the actor.
        GreenfootImage image = getImage();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        for (int i = 0; i < length; i += imageWidth) {
            image.drawImage(image, 0, i);
        }
    }

}
