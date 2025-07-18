package plane.scnu.element;

import plane.scnu.controller.GameController;
import plane.scnu.manager.Award;

import java.util.Random;

/**
 * 蜜蜂（既是敌机也是奖励）
 */
public class Gift extends EnemyPlane implements Award {
    // 特殊移动属性
    private double verticalSpeed;

    public Gift() {
        life = 2; // 生命值较低
        verticalSpeed = new Random().nextDouble() * 3.5 + 0.5;
    }

    // 特殊移动方式 (左右摇摆)
    @Override
    public void move() {
        x -= step;
        y += verticalSpeed;

        // 碰到边界反弹
        if (y > GameController.SCREEN_HEIGHT - height) {
            verticalSpeed = -Math.abs(verticalSpeed);
        }
        if (y < 0) {
            verticalSpeed = Math.abs(verticalSpeed);
        }
    }

    // 奖励类型
    @Override
    public int getAward() {
        Random rand = new Random();
        // 双倍子弹概率100% (固定返回DOUBLE_FIRE)
        // 血量奖励概率10% (当随机数<0.1时额外返回LIFE)
        return (rand.nextDouble() < 0.1) ? Award.LIFE : Award.DOUBLE_FIRE;
    }
    // 得分
    @Override
    public int getScore() {
        return 20; // 击毁得20分
    }
}