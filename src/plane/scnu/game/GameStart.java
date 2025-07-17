package plane.scnu.game;

import plane.scnu.controller.GameThread;

import javax.swing.*;

public class GameStart {

    public static void main(String[] args) {
        //1.显示画框（外层）:JFrame
        JFrame jf = new JFrame();

        //2.显示面板:Jpanel
        GameThread jp =new GameThread();

        //3.将面板放入画框中:
        jf.add(jp);

        //对窗口进行设置
        jf.setTitle("我的窗口");//设置标题
        jf.setSize(436,700); //设置窗口大小
        jf.setLocationRelativeTo(null); //居中显示
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置关闭窗口后自动结束程序

        //4.显示窗口
        jf.setVisible(true);
        jp.action();
    }
}
