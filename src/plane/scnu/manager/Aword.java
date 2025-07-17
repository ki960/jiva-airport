package plane.scnu.manager;
//获得奖励的接口
public interface Aword {
    int DOUBLE_FIRE = 1; //第一种奖励，双倍火力
    int LIFE = 2; //加生命值
   int getAword(); //获得奖励的方法
}