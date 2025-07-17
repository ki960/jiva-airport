package plane.scnu.element;
 
import plane.scnu.manager.Images;

import javax.swing.*;
import java.awt.*;
 
//飞行物的总类(父类)
public abstract class FlyingObjesct {
    public static final int LIVING = 1; //活着
    public static final int DEAD = 0; //死亡
    public static final int WAIT = -1;  //等待死亡（播放爆炸动画）
    //飞机默认是活着的
    public int state = LIVING;
    //飞机的生命值
    public int life = 2;
    public double x,y,step,w,h;//step速度
    public ImageIcon icon; //图片
//    Images images;
    public ImageIcon[] images; //数组
    public int index = 0;
    public ImageIcon [] boms = Images.bom; //存放爆炸效果的图片
    //赋初值：构造方法
    //快速生成构建方法：快捷键Alt+Insert
    public FlyingObjesct(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
 
    }
    //当飞机击打一次，生命值减一，当生命值为0时，进入死亡状态
    public boolean attack(){
        if(life>0){
           life--;
           if(life==0){
               state = DEAD;
           }
           return true;
        }
        return false;
    }
//让敌机快速去死
    public boolean goDead(){
        if(state==LIVING){
            life = 0;
            state = DEAD;
            return true;
        }else{
            return false;
        }
    }
    //判断飞机的三种状态
    public boolean isLiving(){
        return state==LIVING;  //判断是否活着
    }
    public boolean isDead(){
        return state==DEAD;  //判断是否死了
    }
    public boolean isWait(){
        return state==WAIT;  //判断是否等待死亡
    }
 
    /*实现动画效果
     * 1.用数组存储动画效果图片
     * 2.初始化数组
     * 3.定义一个计数器，作为切换图片的控制条件
     * 4.写一个方法，实现切换图片的操作
     * */
    int i;
    public void nextImage(){
        switch (state){
            case LIVING:
                if(images == null) {
                    return;
                }
                icon = images[index++/300%images.length]; //0-[数组的长度-1]
                break;
            case DEAD:
                if(boms==null){
                    return;
                }
                if(i++/300==boms.length){
                    state = WAIT;
                    return;
                }
                icon = boms[i++/300];
        }
 
 
    }
 
    public FlyingObjesct(){}//无参构造
 
    //移动
    public abstract void move();
 
    //绘制图形 方法
    public void painting(Graphics g){
        nextImage();
        icon.paintIcon(null,g,(int)x,(int)y);
    }
    //检测敌机是否碰到子弹：判断条件中心距c=H/2+h/2
    public boolean bong(FlyingObjesct bullet){
        FlyingObjesct p =this;
        double a = Math.min(p.w,p.h)/2;
        double b = Math.min(bullet.w,bullet.h)/2;
        double x1=p.x+p.w/2;
        double y1=p.y+p.h/2;
        double x2=bullet.x+bullet.w/2;
        double y2=bullet.y+bullet.h/2;
        double c = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
        return c<=a+b;
    }
    //清除越界的敌机和子弹条件: y<-h-100 || y>700+100
    public boolean outOfBound(){
        if(y<-h-100){
            return true;
        }else if(y>700+100){
            return true;
        }
        return false;
    }
}