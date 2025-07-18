package plane.scnu.element;

import plane.scnu.controller.GameController;

/**
 * 英雄机
 */
public class Hero extends FlyingObject {
    // 特殊状态
    private int fireCounter = 0;
    private boolean invincible = false;
    private int invincibleTime = 0;
    private boolean tripleFire = false;
    private int tripleFireTime = 0;

    // 特殊状态访问
    public boolean isInvincible() {
        return invincible;
    }

    public boolean isTripleFire() {
        return tripleFire;
    }

    // 激活特殊能力
    public void activateInvincible(int duration) {
        invincible = true;
        invincibleTime = duration;
    }

    public void activateTripleFire(int duration) {
        tripleFire = true;
        tripleFireTime = duration;
    }

    // 更新技能状态
    public void updateSkills() {
        if (invincibleTime > 0) {
            invincibleTime--;
            if (invincibleTime == 0) {
                invincible = false;
            }
        }

        if (tripleFireTime > 0) {
            tripleFireTime--;
            if (tripleFireTime == 0) {
                tripleFire = false;
            }
        }
    }

    // 开火方法
    public Bullet[] fireTriple() {
        double centerX = this.x + this.width;
        double centerY = this.y + this.height / 2;
        return new Bullet[] {
                new Bullet(centerX, centerY - 20),
                new Bullet(centerX, centerY),
                new Bullet(centerX, centerY + 20)
        };
    }

    public Bullet fire() {
        double centerX = this.x + this.width;
        double centerY = this.y + this.height / 2;
        return new Bullet(centerX, centerY);
    }

    // 移动控制
    @Override
    public void move() {} // 基本移动方法留空

    public void move(int mouseX, int mouseY) {
        this.x = mouseX - width / 2;
        this.y = mouseY - height / 2;

        // 边界限制
        if (x < 0) x = 0;
        if (x > GameController.SCREEN_WIDTH - width)
            x = GameController.SCREEN_WIDTH - width;
        if (y < 0) y = 0;
        if (y > GameController.SCREEN_HEIGHT - height)
            y = GameController.SCREEN_HEIGHT - height;
    }

    // 双倍火力状态管理
    private int doubleTime = 0;

    public void getDoubleFire() {
        doubleTime = 280;
    }

    public void updateFireState() {
        if (doubleTime > 0) {
            doubleTime--;
        }
    }

    // 判断是否可以开火
    public boolean canFire() {
        fireCounter++;
        int baseInterval = 17; // 从15调整为17帧
        // 双倍火力射速提升从60%调整为50%
        int actualInterval = doubleTime > 0 ?
                (int)(baseInterval * 2.0/3) : // 相当于提升50%射速
                baseInterval;

        if (fireCounter >= actualInterval) {
            fireCounter = 0;
            return true;
        }
        return false;
    }
}