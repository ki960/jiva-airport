package plane.scnu.element;

import plane.scnu.manager.Images;

public class Hero  extends EnemyPlane {
    public Hero(){
        this.images = Images.hero;
        x=431/2-images[0].getIconWidth()/2;
        y=510;
        w=images[0].getIconWidth();
        h=images[0].getIconHeight();
//        this.speed=0.2;
        this.icon = Images.hero[0];
    }
    public Hero(double x, double y, double w, double h,double step) {
//        super(x, y, w, h);
        this.step=step;
//        this.icon = new ImageIcon("images\\hero1.png"); //设置了Images类直接调用类名就可以引用图片
        this.icon = Images.hero[0];
    }
    @Override
    public void move() {}
    //接收鼠标的坐标
    public void move(int x,int y) {
        this.x= x-images[0].getIconWidth()/2;
        this.y = y-images[0].getIconHeight()/2;
    }
    /*
     * 子弹的位置（在英雄机上） b.x = h.x-images[0].getIconWidth()/2; b.y=h.y
     * 子弹的移动:向上：b.y-=b.step;
     * 子弹是无限的：数序扩容
     * 射击方法：当英雄机调用一次射击方法时，就发射一个子弹
     * */
    //射击的方法
    public Bullet fire(){
        double x = this.x+w/2-4; //获取英雄机的x坐标
        Bullet bullet = new Bullet(x,y);//将处理过后的坐标传给子弹
        return bullet; //将子弹返回
    }
    //获取双倍子弹的方法
    int doubleTime = 0; // 双倍子弹的时间，20次
    public void getdouble_fire(){
        doubleTime = 20;
    }
    //双倍子弹方法
    public Bullet[] double_fire(){
        if(doubleTime>0){
            double x = this.x+w/2-7;
            double x1 = this.x+w/2-2; //获取英雄机的x坐标
            Bullet bullet1 = new Bullet(x,y);
            Bullet bullet2 = new Bullet(x1,y);//将处理过后的坐标传给子弹
            Bullet [] bullets = new Bullet[]{bullet1,bullet2};
//            return new Bullet[]{bullet1,bullet2};
            return bullets;
        }
        else {
            double x = this.x+w/2-4; //获取英雄机的x坐标
            Bullet bullet1 = new Bullet(x,y);
            return new Bullet[]{bullet1};
        }
//
    }
 
    //测试
//    public static void main(String[] args) {
//        Hero hero = new Hero();
//        hero.move(200,300);
//        Bullet bullet = hero.fire();
//        System.out.println(bullet.x+"  "+bullet.y);
//    }
}