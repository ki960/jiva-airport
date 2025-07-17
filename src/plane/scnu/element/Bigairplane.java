package plane.scnu.element;

import plane.scnu.manager.Enemy;
import plane.scnu.manager.Images;

//大飞机类
public class Bigairplane extends EnemyPlane implements Enemy {
    public Bigairplane(double x, double y, double w, double h,double step) {
//        super(x, y, w, h);
        this.step=step;
        this.icon = Images.bigairplane[0];
    }
    public Bigairplane(){
        this.images = Images.bigairplane; //初始化数组
        w = images[0].getIconWidth();
        h = images[0].getIconHeight();
        x=(int)(Math.random()*(420-images[0].getIconWidth()));
        y=-2*images[0].getIconHeight();
        this.step=(Math.random()*3.5+0.7);
//        this.icon = Images.bigairplane[0];
        life = 4;
    }
 
    @Override
    public int getScore() {
        return 50;
    }
}