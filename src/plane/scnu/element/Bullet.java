package plane.scnu.element;

import plane.scnu.manager.Images;

//继承FlyingObjesct类，得到FlyingObjesct类的方法及属性
public class Bullet extends EnemyPlane {
    public Bullet(double x,double y){
        this.icon = Images.bullet;
        w = icon.getIconWidth();
        h = icon.getIconHeight();
        this.x = x;
        this.y = y;
        step = 2;
    }
    public Bullet(){
        x=(int)(Math.random()*370);
        y=(int)(Math.random()*370);
        this.step = 0.2;
        this.icon = Images.bullet;
    }
    public Bullet(double x, double y, double w, double h, double step) {
//        super(x, y, w, h); //重载：调用父类的构造方法，实现方法复用
        this.step = step;
        this.icon = Images.bullet; //背景是固定的
    }
 
    @Override
    public void move() {
        y-=step;
    }
}