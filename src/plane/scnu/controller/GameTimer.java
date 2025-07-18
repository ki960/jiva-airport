package plane.scnu.controller;

import java.util.TimerTask;

public class GameTimer {
    private final GameController controller;
    private final java.util.Timer timer;

    public GameTimer(GameController controller) {
        this.controller = controller;
        this.timer = new java.util.Timer();
    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                controller.gameLoop();
            }
        }, 0, 1000 / 60);
    }
}