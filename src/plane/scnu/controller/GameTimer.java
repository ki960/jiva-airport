package plane.scnu.controller;

import java.util.TimerTask;

/**
 * 游戏定时器 - 负责驱动游戏循环
 */
public class GameTimer {
    private final GameController controller;
    private final java.util.Timer timer;

    /**
     * 构造函数
     * @param controller 游戏控制器实例
     */
    public GameTimer(GameController controller) {
        this.controller = controller;
        this.timer = new java.util.Timer();
    }

    /**
     * 启动游戏循环
     */
    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 调用控制器的游戏循环
                controller.gameLoop();
            }
        }, 0, 1000 / 60); // 以60FPS运行游戏
    }

}