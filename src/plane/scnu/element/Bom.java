package plane.scnu.element;


import plane.scnu.manager.Images;

public class Bom extends EnemyPlane {
    public Bom(){
        x=(int)(Math.random()*370);
        y=(int)(Math.random()*370);
        this.step = 0.2;
        this.icon = Images.bom[0];
    }
    public Bom(double x, double y, double w, double h, double speed) {//要重写父类的构造方法。新增一个速度
//        super(x, y, w, h);//重载：调用父类的构造方法，实现方法复用
        this.step=speed;
        this.icon=Images.bom[0];
    }
}
 