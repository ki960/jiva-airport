package mission.element;
import mission.controller.GameController;
import mission.manager.ResourceManager;
import javax.swing.*;
import java.awt.*;

public abstract class FlyingObject {
    public static final int LIVING = 1;
    public static final int DEAD = 0;
    public static final int WAIT = -1;
    protected int state = LIVING;
    protected int life = 1;
    protected double x, y, step;
    protected double width, height;
    protected ImageIcon icon;
    protected ImageIcon[] images;
    protected int index = 0;
    protected ImageIcon[] bombs;
    protected int animationSpeed = 8;
    protected int bombIndex;

    static {
        ResourceManager.getAnimation("bom");
    }

    public FlyingObject() {
        this.bombs = ResourceManager.getAnimation("bom");
    }

    public void attack() {
        if (life > 0) {
            life--;
            if (life == 0) state = DEAD;
        }
    }

    public void goDead() {
        if (state == LIVING) {
            life = 0;
            state = DEAD;
        }
    }

    public boolean isLiving() {
        return state != LIVING;
    }

    public boolean isDead() {
        return state == DEAD;
    }

    public boolean isWait() {
        return state == WAIT;
    }

    public void nextImage() {
        switch (state) {
            case LIVING:
                if (images == null || images.length == 0) return;
                icon = images[(index++ / animationSpeed) % images.length];
                break;
            case DEAD:
                if (bombs == null || bombs.length == 0) return;
                if (bombIndex / animationSpeed >= bombs.length) {
                    state = WAIT;
                    return;
                }
                icon = bombs[bombIndex++ / animationSpeed];
        }
    }

    public void painting(Graphics g) {
        nextImage();
        if (icon != null) icon.paintIcon(null, g, (int) x, (int) y);
    }

    public boolean bong(FlyingObject other) {
        double centerX1 = x + width / 2;
        double centerY1 = y + height / 2;
        double centerX2 = other.x + other.width / 2;
        double centerY2 = other.y + other.height / 2;
        double radius1 = Math.min(width, height) / 2;
        double radius2 = Math.min(other.width, other.height) / 2;
        double distance = Math.sqrt(Math.pow(centerX2 - centerX1, 2) + Math.pow(centerY2 - centerY1, 2));
        return distance <= (radius1 + radius2);
    }

    public boolean outOfBound() {
        return x < -100 || x > GameController.SCREEN_WIDTH + 100;
    }

    public void move() {}

    public void setImages(ImageIcon[] images) {
        this.images = images;
        if (images != null && images.length > 0) this.icon = images[0];
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public double getHeight() {
        return height;
    }

    public void setStep(double step) {
        this.step = step;
    }
}