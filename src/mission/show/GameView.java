package mission.show;

import mission.controller.GameController;
import mission.element.FlyingObject;
import mission.manager.ResourceManager;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JPanel {
    private final GameController controller;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    private static final int UI_CORNER_X = 20;
    private static final int UI_CORNER_Y = SCREEN_HEIGHT - 160;
    private static final int LIFE_X = UI_CORNER_X;
    private static final int LIFE_Y = UI_CORNER_Y + 70;
    private static final int VALUE_OFFSET_X = 85;
    private static final int SCORE_X = UI_CORNER_X;
    private static final int SCORE_Y = UI_CORNER_Y;
    private static final int SKILL_X = UI_CORNER_X + 170;
    private static final int SKILL_Y = UI_CORNER_Y;
    private static final int SKILL_SPACING = 120;
    private static final int TEXT_OFFSET_Y = 140;

    private int animationCounter = 0;
    private int scoreAnimation = 0;
    private int lastScore = 0;
    private int lifeAnimation = 0;
    private int lastLife = 0;
    private int skill1Animation = 0;
    private int skill2Animation = 0;

    public GameView(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(false);
    }

    private String getRomanLife(int life) {
        life = Math.min(life, 10);
        return switch (life) {
            case 0 -> "-";
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(life);
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        controller.getBackground().painting(g);
        drawGameEntities(g);
        drawUI(g);
        drawGameState(g);
        animationCounter++;
        drawTimeHint(g);
    }

    private void drawGameEntities(Graphics g) {
        List<FlyingObject> enemiesCopy = new ArrayList<>(controller.getEnemies());
        List<FlyingObject> bulletsCopy = new ArrayList<>(controller.getBullets());
        controller.getHero().painting(g);
        for (FlyingObject enemy : enemiesCopy) enemy.painting(g);
        for (FlyingObject bullet : bulletsCopy) bullet.painting(g);
    }

    private void drawUI(Graphics g) {
        drawLifeUI(g);
        drawScoreUI(g);
        drawSkillsUI(g);
    }

    private void drawLifeUI(Graphics g) {
        int currentLife = controller.getLife();
        if (currentLife != lastLife) {
            lifeAnimation = 20;
            lastLife = currentLife;
        }
        int floatOffset = 0;
        if (lifeAnimation > 0) {
            floatOffset = (int)(5 * Math.sin(lifeAnimation * 0.3));
            lifeAnimation--;
        }
        ResourceManager.LIFE_ICON.paintIcon(this, g, LIFE_X, LIFE_Y + floatOffset);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setFont(new Font("Calibri", Font.BOLD, 28));
        if (currentLife > lastLife) g2d.setColor(new Color(0, 200, 0));
        else if (currentLife < lastLife) g2d.setColor(new Color(220, 0, 0));
        else g2d.setColor(Color.WHITE);
        float scale = 1.0f;
        if (lifeAnimation > 0) scale = 1.0f + 0.1f * (lifeAnimation / 20.0f);
        AffineTransform original = g2d.getTransform();
        g2d.translate(LIFE_X + VALUE_OFFSET_X, LIFE_Y + 40 + floatOffset);
        g2d.scale(scale, scale);
        g2d.translate(-(LIFE_X + VALUE_OFFSET_X), -(LIFE_Y + 40 + floatOffset));
        g2d.drawString(" " + getRomanLife(currentLife),
                LIFE_X + VALUE_OFFSET_X,
                LIFE_Y + 40 + floatOffset);
        g2d.setTransform(original);
        g2d.dispose();
    }

    private void drawScoreUI(Graphics g) {
        int currentScore = controller.getScore();
        if (currentScore != lastScore) {
            scoreAnimation = 20;
            lastScore = currentScore;
        }
        float scale = 1.0f;
        if (scoreAnimation > 0) {
            scale = 1.0f + 0.5f * (scoreAnimation / 20.0f);
            scoreAnimation--;
        }
        ResourceManager.SCORE_ICON.paintIcon(this, g, SCORE_X, SCORE_Y);
        int iconWidth = ResourceManager.SCORE_ICON.getIconWidth();
        Graphics2D g2d = (Graphics2D) g.create();
        int floatOffset = (int)(0 * Math.sin(animationCounter * 0.08));
        Font font = new Font("Calibri", Font.BOLD, (int)(24 * scale));
        g2d.setFont(font);
        if (scale > 1.0f) {
            Color startColor = new Color(255, 215, 0);
            Color endColor = Color.WHITE;
            int textStartX = SCORE_X + iconWidth + 5;
            GradientPaint gp = new GradientPaint(
                    textStartX, SCORE_Y,
                    startColor,
                    textStartX, SCORE_Y + 45,
                    endColor
            );
            g2d.setPaint(gp);
        } else g2d.setColor(Color.WHITE);
        int textX = SCORE_X + iconWidth - 5;
        g2d.drawString("× " + currentScore, textX, SCORE_Y + 47 + floatOffset);
        g2d.dispose();
    }

    private void drawSkillsUI(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", Font.BOLD, 20));
        drawSkill(g, ResourceManager.SKILL1_ICON, controller.getSkill1Cost(), SKILL_X, 1, skill1Animation);
        drawSkill(g, ResourceManager.SKILL2_ICON, controller.getSkill2Cost(), SKILL_X + SKILL_SPACING, 2, skill2Animation);
        if (skill1Animation > 0) skill1Animation--;
        if (skill2Animation > 0) skill2Animation--;
    }

    private void drawSkill(Graphics g, ImageIcon icon, int cost, int x, int skillType, int animation) {
        icon.paintIcon(this, g, x, SKILL_Y);
        Graphics2D g2d = (Graphics2D) g.create();
        String text = "Cost:" + cost;
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int textX = x + (icon.getIconWidth() - textWidth) / 2;
        int textY = SKILL_Y + TEXT_OFFSET_Y;
        AffineTransform originalTransform = g2d.getTransform();
        float scale = 1.0f;
        if (animation > 0) {
            float progress = 1.0f - (animation / 20.0f);
            scale = 1.0f + 0.5f * (float)Math.sin(progress * Math.PI);
        }
        g2d.translate(textX + textWidth/2, textY);
        g2d.scale(scale, scale);
        g2d.translate(-(textX + textWidth/2), -textY);
        if (animation > 0) {
            if (skillType == 1) g2d.setColor(new Color(100, 180, 255));
            else g2d.setColor(new Color(255, 100, 100));
        } else g2d.setColor(Color.WHITE);
        g2d.drawString(text, textX, textY);
        g2d.setTransform(originalTransform);
        g2d.dispose();
    }

    private void drawGameState(Graphics g) {
        int centerX = (SCREEN_WIDTH - ResourceManager.START.getIconWidth()) / 2;
        int centerY = (SCREEN_HEIGHT - ResourceManager.START.getIconHeight()) / 2;
        switch (controller.getGameState()) {
            case GameController.READY -> ResourceManager.START.paintIcon(this, g, centerX, centerY);
            case GameController.PAUSE -> ResourceManager.PAUSE.paintIcon(this, g, centerX, centerY);
            case GameController.FAILED -> ResourceManager.FAILED.paintIcon(this, g, centerX, centerY);
            case GameController.WIN -> ResourceManager.WIN.paintIcon(this, g, centerX, centerY);
        }
    }

    private void drawTimeHint(Graphics g) {
        int hintType = controller.getTimeHintType();
        if (hintType == 0) return;

        ImageIcon hintIcon = null;
        switch (hintType) {
            case 1 -> hintIcon = ResourceManager.TIME_30S_HINT;
            case 2 -> hintIcon = ResourceManager.TIME_10S_HINT;
        }
        if (hintIcon == null) return;

        int displayTime = controller.getHintDisplayTime();
        int alpha = (int) (255 * (displayTime / 120f));

        Image image = hintIcon.getImage();
        int width = hintIcon.getIconWidth();
        int height = hintIcon.getIconHeight();

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255.0f));

        int x = (SCREEN_WIDTH - width) / 2;
        int y = (SCREEN_HEIGHT - height) / 2;

        g2d.drawImage(image, x, y, null);
        g2d.dispose();
    }

    public void markSkillUsed(int skillType) {
        if (skillType == 1) skill1Animation = 20;
        else if (skillType == 2) skill2Animation = 20;
    }
}