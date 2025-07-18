package plane.scnu.element;

/**
 * 小敌机
 */
public class Airplane extends EnemyPlane {
    @Override
    public int getScore() {
        return 10; // 击毁得10分
    }
}