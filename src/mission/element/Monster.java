package mission.element;
import mission.manager.Enemy;

public abstract class Monster extends FlyingObject implements Enemy {
    @Override
    public void move() {
        x -= step;
    }
}