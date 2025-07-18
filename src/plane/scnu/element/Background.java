package plane.scnu.element;

import java.awt.*;

/**
 * 游戏背景
 */
public class Background extends FlyingObject {
    // 第二背景位置 (实现滚动效果)
    private double bg2X;

    public Background() {
        step = 0.5; // 背景移动速度较慢
    }

    // 特殊绘制方法 (绘制两张背景图)
    @Override
    public void painting(Graphics g) {
        if (icon != null) {
            icon.paintIcon(null, g, (int) x, (int) y);
            icon.paintIcon(null, g, (int) bg2X, (int) y);
        }
    }

    // 特殊移动方式 (实现无缝滚动)
    @Override
    public void move() {
        x -= step;
        bg2X -= step;

        // 循环背景
        if (x <= -width) {
            x = width;
        }
        if (bg2X <= -width) {
            bg2X = width;
        }
    }

    // 设置背景位置
    public void setBackgroundPosition() {
        bg2X = -width;
    }
}