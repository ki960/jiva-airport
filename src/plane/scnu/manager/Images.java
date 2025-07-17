package plane.scnu.manager;
 
import javax.swing.*;
import java.awt.*;

public class Images {
    public static ImageIcon bg;
    public static ImageIcon bullet;
    public static ImageIcon start;
    public static ImageIcon pause;
    public static ImageIcon gameover;
    public static ImageIcon[] hero;
    public static ImageIcon[] bigairplane;
    public static ImageIcon[] bee;
    public static ImageIcon[] airplane;
    public static ImageIcon[] bom;
    public static ImageIcon lifeIcon;  // 添加生命图标
    public static ImageIcon scoreIcon; // 添加分数图标
    static {
        airplane = new ImageIcon[2];
        airplane[0] = new ImageIcon("images/airplane0.png");
        airplane[1] = new ImageIcon("images/airplane1.png");
        bigairplane = new ImageIcon[2];
        bigairplane[0] = new ImageIcon("images/bigairplane0.png");
        bigairplane[1] = new ImageIcon("images/bigairplane1.png");
        bee = new ImageIcon[2];
        bee[0] = new ImageIcon("images\\bee0.png");
        bee[1] = new ImageIcon("images\\bee1.png");
        bom = new ImageIcon[4];
        bom[0] = new ImageIcon("images/bom1.png");
        bom[1] = new ImageIcon("images/bom2.png");
        bom[2] = new ImageIcon("images/bom3.png");
        bom[3] = new ImageIcon("images/bom4.png");
        bg = new ImageIcon("images\\background.png");
        bullet = new ImageIcon("images\\bullet.png");
        start = new ImageIcon("images\\start.png");
        pause = new ImageIcon("images\\pause.png");
        gameover = new ImageIcon("images\\gameover.png");
        hero = new ImageIcon[2];
        hero[0] = new ImageIcon("images\\hero0.png");
        hero[1] = new ImageIcon("images\\hero1.png");
        lifeIcon = resizeIcon(new ImageIcon("images/life.png"), 30, 30);
        scoreIcon = resizeIcon(new ImageIcon("images/score.png"), 30, 30);
    }
    //测试图片是否传过来了
    public static void main(String[] args) {
        System.out.println(start.getImageLoadStatus()); //如果输出8那图片传过来了，输出其他数字有问题
        System.out.println(bg.getImageLoadStatus());
        System.out.println(bom[0].getImageLoadStatus());
        System.out.println(bee[1].getImageLoadStatus());
        System.out.println(airplane[1].getImageLoadStatus());
 
    }

    // 辅助方法：调整图标大小
    private static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}