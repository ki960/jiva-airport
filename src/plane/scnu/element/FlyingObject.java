package plane.scnu.element;

import plane.scnu.controller.GameController;
import plane.scnu.manager.ResourceManager;

import javax.swing.*;
import java.awt.*;

/**
 * 飞行物基类
 */
public abstract class FlyingObject {
    // 状态常量
    public static final int LIVING = 1;
    public static final int DEAD = 0;
    public static final int WAIT = -1;

    // 对象属性
    protected int state = LIVING;
    protected int life = 1;
    protected double x, y, step;
    protected double width, height;
    protected ImageIcon icon;
    protected ImageIcon[] images;
    protected int index = 0;
    protected ImageIcon[] bombs;

    static {
        ResourceManager.getAnimation("bom");
    }

    protected int animationSpeed = 5;
    // 状态管理
    public void attack() {
        if (life > 0) {
            life--;
            if (life == 0) {
                state = DEAD;
            }
        }
    }

    public void goDead() {
        if (state == LIVING) {
            life = 0;
            state = DEAD;
        }
    }

    public boolean isLiving() {
        return state == LIVING;
    }

    public boolean isDead() {
        return state == DEAD;
    }

    public boolean isWait() {
        return state == WAIT;
    }

    // 动画管理
    protected int bombIndex;
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

    // 绘制方法
    public void painting(Graphics g) {
        nextImage();
        if (icon != null) {
            icon.paintIcon(null, g, (int) x, (int) y);
        }
    }

    // 碰撞检测
    public boolean bong(FlyingObject other) {
        // 计算两物体中心点距离
        double centerX1 = x + width / 2;
        double centerY1 = y + height / 2;
        double centerX2 = other.x + other.width / 2;
        double centerY2 = other.y + other.height / 2;

        // 计算两物体半径
        double radius1 = Math.min(width, height) / 2;
        double radius2 = Math.min(other.width, other.height) / 2;

        // 计算圆心距离
        double distance = Math.sqrt(Math.pow(centerX2 - centerX1, 2) + Math.pow(centerY2 - centerY1, 2));

        return distance <= (radius1 + radius2);
    }

    // 边界检测
    public boolean outOfBound() {
        return x < -100 || x > GameController.SCREEN_WIDTH + 100;
    }

    // 移动方法 (由子类实现)
    public void move() {
    }

    // Setter方法
    public void setImages(ImageIcon[] images) {
        this.images = images;
        if (images != null && images.length > 0) {
            this.icon = images[0];
        }
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

    public double getHeight() { return height; }

    public FlyingObject() {
        this.bombs = ResourceManager.getAnimation("bom"); // 初始化爆炸动画
    }

    public void setStep(double step) {
        this.step = step;
    }
}