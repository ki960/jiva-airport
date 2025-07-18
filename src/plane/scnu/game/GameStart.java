package plane.scnu.game;

import plane.scnu.controller.GameController;
import javax.swing.*;

public class GameStart {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GameController controller = new GameController();

        // 设置内容面板为1280x720
        frame.setContentPane(controller.getView());

        frame.setResizable(false);
        // 自动调整窗口大小以包含装饰
        frame.pack();

        // 设置窗口位置居中
        frame.setLocationRelativeTo(null);

        // 其他设置保持不变
        frame.setTitle("The Barents Witch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        controller.start();
    }
}