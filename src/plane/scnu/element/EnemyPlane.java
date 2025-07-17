package plane.scnu.element;

public abstract class EnemyPlane extends FlyingObjesct {
    @Override
    public void move() {
        y+=step;
    }
}