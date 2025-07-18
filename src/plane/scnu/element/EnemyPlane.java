package plane.scnu.element;
import plane.scnu.manager.Enemy;

public abstract class EnemyPlane extends FlyingObject implements Enemy {
    @Override
    public void move() {
        x -= step;
    }
}