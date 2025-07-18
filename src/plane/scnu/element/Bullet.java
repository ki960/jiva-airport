package plane.scnu.element;

import plane.scnu.controller.GameController;
import plane.scnu.manager.ResourceManager;

/**
 * 子弹
 */
public class Bullet extends FlyingObject {

    public Bullet(double x, double y) {
        this.step = 3.5;
        this.icon = ResourceManager.BULLET; // 添加资源设置
        this.width = icon.getIconWidth();
        this.height = icon.getIconHeight();
        this.x = x;
        this.y = y;
    }

    // 移动方式 (水平向右)
    @Override
    public void move() {
        x += step;
    }

    // 边界检测 (更严格)
    @Override
    public boolean outOfBound() {
        return x > GameController.SCREEN_WIDTH;
    }
}