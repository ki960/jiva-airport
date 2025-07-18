package plane.scnu.manager;

import plane.scnu.controller.GameController;
import plane.scnu.element.*;

import java.util.Random;

public class ObjectFactory {
    public static EnemyPlane createEnemy(int type) {
        EnemyPlane plane = switch (type) {
            case 9 -> createBee();
            case 6, 7, 8 -> createBigAirplane();
            default -> createAirplane();
        };

        // 确保先设置尺寸再计算位置
        int yPos = new Random().nextInt(GameController.SCREEN_HEIGHT - (int)plane.getHeight());
        plane.setPosition(GameController.SCREEN_WIDTH, yPos);
        return plane;
    }

    // 创建特定敌机类型
    public static Airplane createAirplane() {
        Airplane airplane = new Airplane();
        airplane.setImages(ResourceManager.getAnimation("airplane"));
        airplane.setWidth(ResourceManager.getAnimation("airplane")[0].getIconWidth());
        airplane.setHeight(ResourceManager.getAnimation("airplane")[0].getIconHeight());
        airplane.setLife(2);
        return airplane;
    }

    public static BigAirplane createBigAirplane() {
        BigAirplane bigAirplane = new BigAirplane();
        bigAirplane.setImages(ResourceManager.getAnimation("bigairplane"));
        bigAirplane.setWidth(ResourceManager.getAnimation("bigairplane")[0].getIconWidth());
        bigAirplane.setHeight(ResourceManager.getAnimation("bigairplane")[0].getIconHeight());
        bigAirplane.setLife(5);
        return bigAirplane;
    }

    public static Gift createBee() {
        Gift gift = new Gift();
        gift.setImages(ResourceManager.getAnimation("bee"));
        gift.setWidth(ResourceManager.getAnimation("bee")[0].getIconWidth());
        gift.setHeight(ResourceManager.getAnimation("bee")[0].getIconHeight());
        gift.setLife(2);
        return gift;
    }

    // 创建英雄机
    public static Hero createHero() {
        Hero hero = new Hero();
        hero.setImages(ResourceManager.getAnimation("hero"));
        hero.setWidth(ResourceManager.getAnimation("hero")[0].getIconWidth());
        hero.setHeight(ResourceManager.getAnimation("hero")[0].getIconHeight());
        // 设置初始位置
        hero.setPosition(50, (436 - hero.getHeight()) / 2.0);
        return hero;
    }

    // 创建背景
    public static Background createBackground() {
        Background background = new Background();
        background.setIcon(ResourceManager.BG);
        background.setWidth(ResourceManager.BG.getIconWidth());
        background.setHeight(ResourceManager.BG.getIconHeight());
        return background;
    }
}