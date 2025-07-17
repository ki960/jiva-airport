package plane.scnu.element;
 
import plane.scnu.manager.Enemy;
import plane.scnu.manager.Images;

import javax.swing.*;
//飞机类
public class Airplane extends EnemyPlane implements Enemy { // 实现接口时，要实现接口中的方法
    public Airplane(){
        this.images = Images.airplane;
        w = images[0].getIconWidth();
        h = images[0].getIconHeight();
        x=(int)(Math.random()*(420-images[0].getIconWidth()));
        y=-2*images[0].getIconHeight();
        this.step=(Math.random()*2.5+0.7);
        this.icon=new ImageIcon("images\\airplane1.png");
//        this.icon = Images.airplane[0];
    }
    public Airplane(double x, double y, double w, double h,double speed) {
//        super(x, y, w, h);
        this.step=speed;
        this.icon=Images.airplane[0];
    }
 
 
    @Override
    public int getScore() {
        return 10;
    }
}