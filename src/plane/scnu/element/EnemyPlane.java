package plane.scnu.element;

import plane.scnu.manager.Enemy;

/**
 * 敌机基类
 */
public abstract class EnemyPlane extends FlyingObject implements Enemy {
    @Override
    public void move() {
        x -= step; // 所有敌机都向左移动
    }
}