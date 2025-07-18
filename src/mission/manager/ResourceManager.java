package mission.manager;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    public static final ImageIcon BG = loadIcon("background.png");
    public static final ImageIcon BULLET = loadIcon("bullet.png");
    public static final ImageIcon START = loadIcon("start.png");
    public static final ImageIcon PAUSE = loadIcon("pause.png");
    public static final ImageIcon FAILED = loadIcon("failed.png");
    public static final ImageIcon LIFE_ICON = resizeIcon(loadIcon("health.png"), 80, 80);
    public static final ImageIcon SCORE_ICON = resizeIcon(loadIcon("coin.png"), 80, 80);
    public static final ImageIcon SKILL1_ICON = resizeIcon(loadIcon("skill1.png"), 91, 120);
    public static final ImageIcon SKILL2_ICON = resizeIcon(loadIcon("skill2.png"), 91, 120);
    public static final ImageIcon WIN = loadIcon("win.png");
    public static final ImageIcon TIME_30S_HINT = loadIcon("30s.png");
    public static final ImageIcon TIME_10S_HINT = loadIcon("10s.png");
    private static final Map<String, ImageIcon[]> animations = new HashMap<>();

    static {
        animations.put("mob", new ImageIcon[]{loadIcon("mob0.png"), loadIcon("mob1.png")});
        animations.put("elite", new ImageIcon[]{loadIcon("elite0.png"), loadIcon("elite1.png")});
        animations.put("gift", new ImageIcon[]{loadIcon("gift0.png"), loadIcon("gift1.png")});
        animations.put("witch", new ImageIcon[]{loadIcon("witch0.png"), loadIcon("witch1.png")});
        animations.put("bom", new ImageIcon[]{loadIcon("bom0.png"), loadIcon("bom1.png"), loadIcon("bom2.png"), loadIcon("bom3.png"), loadIcon("bom4.png"), loadIcon("bom5.png")});
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