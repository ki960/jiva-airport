package mission.element;
import mission.controller.GameController;
import mission.manager.Award;
import java.util.Random;

public class Gift extends Monster implements Award {
    private double verticalSpeed;

    public Gift() {
        verticalSpeed = new Random().nextDouble() * 3.5 + 1;
    }

    @Override
    public void move() {
        x -= step;
        y += verticalSpeed;
        if (y > GameController.SCREEN_HEIGHT - height) verticalSpeed = -Math.abs(verticalSpeed);
        if (y < 0) verticalSpeed = Math.abs(verticalSpeed);
    }

    @Override
    public int getAward() {
        Random rand = new Random();
        return (rand.nextDouble() < 0.1) ? Award.LIFE : Award.DOUBLE_FIRE;
    }

    @Override
    public int getScore() {
        return 20;
    }
}