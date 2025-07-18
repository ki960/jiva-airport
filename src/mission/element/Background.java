package mission.element;
import java.awt.*;

public class Background extends FlyingObject {
    private double bg2X;

    public Background() {
        step = 0.5;
    }

    @Override
    public void painting(Graphics g) {
        if (icon != null) {
            icon.paintIcon(null, g, (int) x, (int) y);
            icon.paintIcon(null, g, (int) bg2X, (int) y);
        }
    }

    @Override
    public void move() {
        x -= step;
        bg2X -= step;
        if (x <= -width) x = width;
        if (bg2X <= -width) bg2X = width;
    }

    public void setBackgroundPosition() {
        bg2X = -width;
    }
}