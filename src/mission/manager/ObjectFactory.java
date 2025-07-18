package mission.manager;
import mission.controller.GameController;
import mission.element.*;
import java.util.Random;

public class ObjectFactory {
    public static Monster createEnemy(int type) {
        Monster plane = switch (type) {
            case 9 -> createGift();
            case 6, 7, 8 -> createBigAirplane();
            default -> createAirplane();
        };
        int yPos = new Random().nextInt(GameController.SCREEN_HEIGHT - (int)plane.getHeight());
        plane.setPosition(GameController.SCREEN_WIDTH, yPos);
        return plane;
    }

    public static Mob createAirplane() {
        Mob mob = new Mob();
        mob.setImages(ResourceManager.getAnimation("mob"));
        mob.setWidth(ResourceManager.getAnimation("mob")[0].getIconWidth());
        mob.setHeight(ResourceManager.getAnimation("mob")[0].getIconHeight());
        mob.setLife(2);
        return mob;
    }

    public static Elite createBigAirplane() {
        Elite elite = new Elite();
        elite.setImages(ResourceManager.getAnimation("elite"));
        elite.setWidth(ResourceManager.getAnimation("elite")[0].getIconWidth());
        elite.setHeight(ResourceManager.getAnimation("elite")[0].getIconHeight());
        elite.setLife(6);
        return elite;
    }

    public static Gift createGift() {
        Gift gift = new Gift();
        gift.setImages(ResourceManager.getAnimation("gift"));
        gift.setWidth(ResourceManager.getAnimation("gift")[0].getIconWidth());
        gift.setHeight(ResourceManager.getAnimation("gift")[0].getIconHeight());
        gift.setLife(4);
        return gift;
    }

    public static Witch createHero() {
        Witch witch = new Witch();
        witch.setImages(ResourceManager.getAnimation("witch"));
        witch.setWidth(ResourceManager.getAnimation("witch")[0].getIconWidth());
        witch.setHeight(ResourceManager.getAnimation("witch")[0].getIconHeight());
        witch.setPosition(50, (436 - witch.getHeight()) / 2.0);
        return witch;
    }

    public static Background createBackground() {
        Background background = new Background();
        background.setIcon(ResourceManager.BG);
        background.setWidth(ResourceManager.BG.getIconWidth());
        background.setHeight(ResourceManager.BG.getIconHeight());
        return background;
    }
}