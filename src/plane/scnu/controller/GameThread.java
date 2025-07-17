package plane.scnu.controller;
 
import plane.scnu.element.*;
import plane.scnu.manager.Aword;
import plane.scnu.manager.Bee;
import plane.scnu.manager.Enemy;
import plane.scnu.manager.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class GameThread extends JPanel {
    //ImageIcon类：保存图片的路径----->读取图片
//    ImageIcon icon = new ImageIcon("images\\background.png");
//    BG bg = new BG(0,0,420,700,2);
//    Airplane airplane = new Airplane(100,200,300,620,2);
//    Bee bee = new Bee(200,100,200,700,2);
    BG bg = new BG();
    EnemyPlane[] planes;
    Hero hero;
    int index; //用来控制敌机的数量
    Bullet[] bullets;  //创建子弹的数组
    int life; //英雄机的生命值
    int score; //分数
    //定义游戏的四种状态：准备，运行，暂停，结束（常量）
    public static final int READY = 0;
    public static final int RUNNING = 1;
    public static final int PAUSE = 2;
    public static final int GAMEOVER = 3;
    private static final int LIFE_X = 20;
    private static final int LIFE_Y = 20;
    private static final int SCORE_X = 20;
    private static final int SCORE_Y = 60;
    private static final int VALUE_OFFSET_X = 35; // 数值在图标右侧的偏移量
    private static final int VALUE_Y_OFFSET = 22;  // 数值垂直居中偏移量
    //游戏的初始状态
    int state = READY;
 
 
    //    Airplane [] airPlanes = new Airplane[10];
//    Bigairplane [] bigAirPlanes = new Bigairplane[10];
//    Bee [] bee = new Bee[10];
//    Bom [] bom = new Bom[4];
//    Bullet [] bullets = new Bullet[3];
//    Hero [] hero =new Hero[2];
    //初始化方法
    public void init(){
        bg = new BG();
        hero = new Hero();
        planes = new EnemyPlane[0]; //默认一个敌机都没有
        bullets = new Bullet[0];  //默认一个子弹都没有
        index = 0; //默认从0开始
        life = 3; //默认3条命
        score = 0; //分数为0
    }
    public GameThread() {
        init();
 
//        for(int i=0;i<planes.length;i++){
//            if(i%2==0){
//                planes[i]=new Airplane();
//            }
//            else if(i%5==0 && i%2!=0){
//                planes[i] = new Bee();
//            }
//            else {
//                planes[i] = new Bigairplane();
//            }
//        }
//        for(int i =0;i<airPlanes.length;i++){
//            airPlanes[i] = new Airplane();
//            bigAirPlanes[i]=new BigAirPlane();
//            bee[i]=new Bee();
//            bom[i]=new Bom();
//            bullets[i]=new Bullet();
//            hero[i]=new Hero();
    }
    //生成敌机的方法
    public void createPlane(){
        if(index%16==0){
            int n = (int)(Math.random()*10);
            EnemyPlane plane;
            switch (n){
                case 9 :
                    plane = new Bee(); //10%生成小飞碟
                    break;
                case 8 :
                case 7 :
                    plane = new Bigairplane(); //20%生成大飞机
                    break;
                default:
                    plane = new Airplane(); //70%生成小飞机
 
            }
            //将敌机存入数组中之前，要先对数组进行扩容处理
            planes = Arrays.copyOf(planes,planes.length+1);
            //将生存的敌机放入数组的最后一个位置
            planes[planes.length-1] = plane;
        }
    }
 
 
//        for(int i =0;i<bigAirPlanes.length;i++){
//            bigAirPlanes[i]=new Bigairplane();
//       }
//    }
 
    //绘制图片printing
    @Override
    public void paint(Graphics g) { //paint挂载到当前类，当前类实例化（创建对象）时自动调用
//      super.paint(g);
//      icon.paintIcon(this,g,0,y1++); //绘制背景，默认第一个绘制
//        bg.icon.paintIcon(this,g,(int)bg.x,(int)bg.y);
//      bg.move();
//        airplane.icon.paintIcon(this,g,(int)airplane.x,(int)airplane.y);
//        bee.icon.paintIcon(this,g,(int)bee.x,(int)bee.y);
        bg.painting(g);
        hero.painting(g);
        for(int i=0;i<planes.length;i++){
            planes[i].painting(g);
        }
        for(int i=0;i<bullets.length;i++){
            bullets[i].painting(g);
        }
//        g.setColor(new Color(255,255,255));
//        //设置字体
//        g.setFont(new Font("微软雅黑",Font.BOLD,20));
//        g.drawString("生命值"+life,20,20);
//        g.drawString("分数"+score,20,40);
        // 绘制生命图标和数值
        Images.lifeIcon.paintIcon(this, g, LIFE_X, LIFE_Y);
        g.setColor(Color.WHITE);
        g.setFont(new Font("微软雅黑", Font.BOLD, 20));
        g.drawString(String.valueOf(life), LIFE_X + VALUE_OFFSET_X, LIFE_Y + VALUE_Y_OFFSET);

        // 绘制分数图标和数值
        Images.scoreIcon.paintIcon(this, g, SCORE_X, SCORE_Y);
        g.drawString(String.valueOf(score), SCORE_X + VALUE_OFFSET_X, SCORE_Y + VALUE_Y_OFFSET);
        if(state==READY){
            Images.start.paintIcon(this,g,0,0);
        }
        if(state==PAUSE){
            Images.pause.paintIcon(this,g,0,0);
        }
        if(state==GAMEOVER){
            Images.gameover.paintIcon(this,g,0,0);
        }

//            for(int i =0;i<airPlanes.length;i++){
//                airPlanes[i].painting(g);
//            bigAirPlanes[i].painting(g);
//            bee[i].painting(g);
//            bom[i].painting(g);
//            bullets[i].painting(g);
//            hero[i].painting(g);
//            }
//            for(int i =0;i<bigAirPlanes.length;i++){
//                bigAirPlanes[i].painting(g);
//            }
        repaint();//刷新窗口
    }

    //唤醒定时器的方法
    public void action(){
        Timer timer = new Timer();
        MyTesk mt = new MyTesk();
        timer.schedule(mt,1000,1000/50);
        this.addMouseListener(new MouseAdapter() {
            //监听鼠标点击
            @Override
            public void mouseClicked(MouseEvent e) {
                if(state==READY){
                    state=RUNNING;
                }
                if(state==GAMEOVER){
                    state=READY;
                    init();
                }
            }
            //鼠标移除
            @Override
            public void mouseExited(MouseEvent e) {
//                System.out.println("鼠标移出");
                if(state==RUNNING){
                    state=PAUSE;
                }
            }
            //鼠标移入
            @Override
            public void mouseEntered(MouseEvent e) {
                if(state==PAUSE){
                    state=RUNNING;
                }
            }
        });
        //匿名内部类
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(state==RUNNING){
                    int x = e.getX();
                    int y = e.getY();
                    hero.move(x,y);
                }
            }
        };
        //安装监听器
        this.addMouseMotionListener(mouseAdapter);
    }
    public void createBullet(){
        if(!hero.isLiving()){ //英雄机死亡不发射子弹
            return;
        }
        if(index%17==0){
            Bullet[] bullets1 = hero.double_fire();
            //俩个数组的合并
           Bullet[] arr = Arrays.copyOf(bullets,bullets.length+bullets1.length);
            System.arraycopy(bullets1,0,arr,bullets.length,bullets1.length);
            bullets = arr;
//            Bullet bullet =hero.fire();  //英雄机调用fire方法生成子弹
//            //将子弹放入数组中
//            bullets = Arrays.copyOf(bullets,bullets.length+1); //子弹扩容
//            bullets[bullets.length-1] = bullet;
        }
    }
    //加分的方法
    public void addScore(FlyingObjesct plane){
        if(plane.isDead()){
            if(plane instanceof Enemy){
                Enemy enemy = (Enemy) plane; //需要做一个强制转化
                score += enemy.getScore();
            }
            if(plane instanceof Aword){
                Aword aword = (Aword) plane;
                int  type =aword.getAword();
                if(type == Aword.DOUBLE_FIRE){
                    hero.getdouble_fire();
                }
                if(type == Aword.LIFE){
                    life++;
                }
            }
        }
    }
    //检测敌机与英雄机的碰撞
    public void hero_hit(){
        if(hero.isLiving()){
            for(int i=0;i<planes.length;i++){
                if(!planes[i].isLiving()){
                    continue;
                }
                if(hero.bong(planes[i])){
                    hero.goDead();
                    planes[i].goDead();
                    break;
                }
            }
        }
        else if(hero.isWait()){ //僵尸状态
            if(life>1){
                //重新开始
                hero = new Hero();
                //清屏
                for(int i=0;i<planes.length;i++){
                    planes[i].goDead();
                }
                life--;
            }

            else{
                life--;
                state=GAMEOVER;
            }

        }
    }
    //检测每个敌机与子弹的碰撞情况
    public void hit(){
        for(int i=0;i<bullets.length;i++){
            Bullet bullet = bullets[i]; //拿出每一颗子弹
            if(!bullet.isLiving()){
                continue;
            }
            for(int j=0;j<planes.length;j++){
                FlyingObjesct p = planes[j];
                if(!p.isLiving()){
                    continue;
                }
                if(p.bong(bullet)){  //被击中
                    p.attack();
                    bullet.goDead();
                    addScore(p);
                }
            }
        }
    }

    //清理爆炸的飞机
    public void clean(){
        //清除飞机
        EnemyPlane[] living = new EnemyPlane[planes.length];
        int index = 0;
        for(int i=0;i<planes.length;i++){  //遍历敌机数组
            if(planes[i].isWait() || planes[i].outOfBound()){  //如果是等待状态的效果就跳过
                continue;
            }
            living[index++] = planes[i];  //将不是等待状态的敌机存在living里面
        }
        planes = Arrays.copyOf(living,index);
        //清除子弹
        Bullet[] livingBullet = new Bullet[bullets.length];
        index = 0;
        for(int i=0;i<bullets.length;i++){
            if(bullets[i].isDead() || bullets[i].outOfBound()){    //如果是已经爆炸的子弹就跳过
                continue;
            }
            livingBullet[index++] = bullets[i];
        }
        bullets = Arrays.copyOf(livingBullet,index);
    }


    class MyTesk extends TimerTask {

        @Override
        public void run() {
            index++;
            if(state == RUNNING){
                createPlane(); //调用生成敌机的方法
                createBullet(); //调用生成子弹的方法
                hit();//调用子弹击打的效果
                clean(); //调用清理敌机方法
                hero_hit();
                bg.move();
                for(int i =0;i<planes.length;i++){
                    planes[i].move();
                }
                for(int i =0;i<bullets.length;i++){
                    bullets[i].move();
                }
            }

        }
    }
}