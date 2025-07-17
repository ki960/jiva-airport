package plane.scnu.element;
 
import plane.scnu.manager.Images;

import java.awt.*;
 
//背景类(子类)
//继承FlyingObjesct类，得到FlyingObjesct类的方法及属性
public class BG extends FlyingObjesct {
    double y0;
    public BG(){
        x = 0;
        y = 0;
        icon = Images.bg;
        w = icon.getIconWidth();
        h = icon.getIconHeight();
        y0 = -h;
        step = 2;
 
    }
    public BG(double x, double y, double w, double h,double step) {
        super(x, y, w, h); //重载：调用父类的构造方法，实现方法复用
        this.step = step;
        this.icon = Images.bg; //背景是固定的
    }
    public void painting(Graphics g){
        icon.paintIcon(null,g,(int)x,(int)y);
        icon.paintIcon(null,g,(int)x,(int)y0);
    }
    @Override
    public void move() {
        y+=step;
        y0+=step;
        if(y>=h){
            y=-h;
        }
        if(y0>=h){
            y0=-h;
        }
    }
}