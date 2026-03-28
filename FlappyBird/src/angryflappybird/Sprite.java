package angryflappybird;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is the class that initialize a sprite
 */
public class Sprite {  

    private Image image;

    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    private String IMAGE_DIR = "../resources/images/";

    /**
     * Here is a Sprite Constructor
     * It constructs a basic sprite without image, width, height
     * only position (0,0) and velocity(0,0)
     */
    public Sprite() {
        this.positionX = 0;
        this.positionY = 0;
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    /**
     * Here is the Sprite Constructor
     * It constructs a basic sprite with image, position
     * and velocity sets to (0,0)
     * @param pX The X position of the sprite
     * @param pY The Y position of the sprite
     * @param image The image of the sprite
     */
    public Sprite(double pX, double pY, Image image) {
    	setPositionXY(pX, pY);
        setImage(image);
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    /** This is to decide if an egg collision. */
    // flag for egg collision
    boolean collided = false;

    /**
     * setCollided method
     * It sets if the egg is collided.
     * @param collided It indicates if the egg is collided
     * @return a void method
     */
    public void setCollided(boolean collided) {
        this.collided = collided;
    }
    
    /** This is to decide if an egg is a golden egg. */
    boolean isGoldenEgg = false;
    
    /**
     * setGoldenEgg method
     * It sets the isGoldenEgg to true if the egg is a golden egg.
     * @param goldenEgg if the egg is a golden egg
     * @return a void method
     */
    public void setGoldenEgg(boolean goldenEgg) {
        isGoldenEgg = goldenEgg;
    }
    
    /**
     * setImage method
     * It sets the image of the sprite
     * It changes the following class parameter: image, width, height
     * @param image The image of the sprite
     * @return a void method
     */
    public void setImage(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
    
    /**
     * setPositionXY method
     * It sets the position of the sprite
     * It changes the following class parameter: positionX, positionY
     * @param positionX The X position of the sprite
     * @param positionY The Y position of the sprite
     * @return a void method
     */
    public void setPositionXY(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
    
    /**
     * getPositionX method
     * It gets the X position of the sprite
     * @param no parameter
     * @return a double, the X position for the sprite
     */
    public double getPositionX() {
        return positionX;
    }
    
    /**
     * getPositionY method
     * It gets the Y position of the sprite
     * @param no parameter
     * @return a double, the Y position for the sprite
     */
    public double getPositionY() {
        return positionY;
    }
    
    /**
     * setVelocity method
     * It sets the velocity of the sprite
     * It changes the following class parameter: velocityX, velocityY
     * @param velocityX The velocity on X of the sprite
     * @param velocityY The velocity on Y of the sprite
     * @return a void method
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    
    /**
     * addVelocity method
     * It adds the velocity of the sprite based on its previous velocity
     * It changes the following class parameter: velocityX, velocityY
     * @param x The change of velocity on X of the sprite
     * @param y The change of velocity on Y of the sprite
     * @return a void method
     */
    public void addVelocity(double x, double y) {
        this.velocityX += x;
        this.velocityY += y;
    }

    /**
     * getVelocityX method
     * It gets the velocity on X of the sprite
     * @param no parameter
     * @return a double, the X velocity of the sprite
     */
    public double getVelocityX() {
        return velocityX;
    }
    
    /**
     * getVelocityY method
     * It gets the velocity on Y of the sprite
     * @param no parameter
     * @return a double, the Y velocity of the sprite
     */
    public double getVelocityY() {
        return velocityY;
    }
    
    /**
     * getWidth method
     * It gets the width of the sprite
     * @param no parameter
     * @return a double, the width of the sprite
     */
    public double getWidth() {
        return width;
    }

    /**
     * render method
     * It draws the sprite on the game scene
     * @param gc, the GraphicsContext of the game scene
     * @return a void method
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }
    
    /**
     * getBoundary method
     * It gets the boundary of a sprite
     * @param no param
     * @return a Rectangle2D for the boundary of the sprite on the scene
     */
    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }
    
    /**
     * intersectsSprite method
     * It gets the if another sprite intersects with current sprite
     * @param s another sprite of the possible collision
     * @return if the two sprites collided
     */
    public boolean intersectsSprite(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    /**
     * update method
     * It updates the position of the sprite according to the velocity and time
     * @param time the time of the game
     * @return no return; it is a void method
     */
    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }


}
