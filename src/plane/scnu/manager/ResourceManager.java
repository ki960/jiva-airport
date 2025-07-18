package plane.scnu.manager;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    public static final ImageIcon BG = loadIcon("background.png");
    public static final ImageIcon BULLET = loadIcon("bullet.png");
    public static final ImageIcon START = loadIcon("start.png");
    public static final ImageIcon PAUSE = loadIcon("pause.png");
    public static final ImageIcon GAMEOVER = loadIcon("gameover.png");
    public static final ImageIcon LIFE_ICON = resizeIcon(loadIcon("character_head.png"), 80, 80);
    public static final ImageIcon SCORE_ICON = resizeIcon(loadIcon("gold_coin.png"), 80, 80);
    public static final ImageIcon SKILL1_ICON = resizeIcon(loadIcon("skill1.png"), 91, 120);
    public static final ImageIcon SKILL2_ICON = resizeIcon(loadIcon("skill2.png"), 91, 120);

    private static final Map<String, ImageIcon[]> animations = new HashMap<>();

    static {
        animations.put("airplane", new ImageIcon[]{loadIcon("airplane0.png"), loadIcon("airplane1.png")});
        animations.put("bigairplane", new ImageIcon[]{loadIcon("bigairplane0.png"), loadIcon("bigairplane1.png")});
        animations.put("bee", new ImageIcon[]{loadIcon("bee0.png"), loadIcon("bee1.png")});
        animations.put("hero", new ImageIcon[]{loadIcon("hero0.png"), loadIcon("hero1.png")});
        animations.put("bom", new ImageIcon[]{loadIcon("bom1.png"), loadIcon("bom2.png"), loadIcon("bom3.png"), loadIcon("bom4.png")});
    }

    private static ImageIcon loadIcon(String filename) {
        return new ImageIcon("images/" + filename);
    }

    public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        if (icon == null) return null;
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public static ImageIcon[] getAnimation(String type) {
        return animations.getOrDefault(type, null);
    }
}