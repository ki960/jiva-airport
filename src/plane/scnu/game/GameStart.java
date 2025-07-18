package plane.scnu.game;

import plane.scnu.controller.GameController;
import javax.swing.*;

public class GameStart {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GameController controller = new GameController();
        frame.setContentPane(controller.getView());
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("The Barents Witch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        controller.start();
    }
}