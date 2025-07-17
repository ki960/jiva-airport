package plane.scnu.manager;

import plane.scnu.element.EnemyPlane;

public class Bee extends EnemyPlane implements Aword {
    double speed1;
    public Bee(){
        this.images = Images.bee;
        w = images[0].getIconWidth();
        h = images[0].getIconHeight();
        x=(int)(Math.random()*(420-images[0].getIconWidth()));
        y=-2*images[0].getIconHeight();
        this.step=(Math.random()*4.5+2.5);
        this.speed1=(Math.random()*3.5+0.5);
//        this.icon=new ImageIcon("images\\bee0.png");
        this.icon=Images.bee[0];
        life = 6;
    }
    public Bee(double x, double y, double w, double h,double step) {
//        super(x, y, w, h);
        this.step=step;
        this.icon = Images.bee[0];
    }
    @Override
    public void move() {
        y+=speed1;
        x+=step;
        if(x>431-images[0].getIconWidth()){
            step = -step;
        }
        if(x<=0){
            step = -step;
        }
    }
 
    @Override
    public int getAword() {
        return Math.random()>0.5?DOUBLE_FIRE:LIFE;
    }
}