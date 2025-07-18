package plane.scnu.element;
import plane.scnu.controller.GameController;
import plane.scnu.manager.ResourceManager;

public class Bullet extends FlyingObject {
    public Bullet(double x, double y) {
        this.step = 3.5;
        this.icon = ResourceManager.BULLET;
        this.width = icon.getIconWidth();
        this.height = icon.getIconHeight();
        this.x = x;
        this.y = y;
    }

    @Override
    public void move() {
        x += step;
    }

    @Override
    public boolean outOfBound() {
        return x > GameController.SCREEN_WIDTH;
    }
}