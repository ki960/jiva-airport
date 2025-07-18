package plane.scnu.element;

/**
 * 大敌机
 */
public class BigAirplane extends EnemyPlane {
    @Override
    public void move() {
        x -= step;  // 添加向左移动逻辑
    }

    public BigAirplane() {
        life = 5; // 生命值较高
    }

    @Override
    public int getScore() {
        return 60; // 击毁得50分
    }
}