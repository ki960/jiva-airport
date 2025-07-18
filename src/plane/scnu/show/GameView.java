package plane.scnu.show;

import plane.scnu.controller.GameController;
import plane.scnu.element.FlyingObject;
import plane.scnu.manager.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private final GameController controller;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    // 新的左下角UI布局常量
    private static final int UI_CORNER_X = 20;
    private static final int UI_CORNER_Y = SCREEN_HEIGHT - 160;
    // 生命和积分位置
    private static final int LIFE_X = UI_CORNER_X;
    private static final int LIFE_Y = UI_CORNER_Y + 70;  // 生命在积分下方
    private static final int VALUE_OFFSET_X = 85;

    // 积分在生命下方
    private static final int SCORE_X = UI_CORNER_X;
    private static final int SCORE_Y = UI_CORNER_Y;
    // 技能位置（稍向右移）
    private static final int SKILL_X = UI_CORNER_X + 170; // 向右移动220像素
    private static final int SKILL_Y = UI_CORNER_Y;
    private static final int SKILL_SPACING = 120; // 技能间距
    private static final int TEXT_OFFSET_Y = 140; // 技能文本Y偏移

    public GameView(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(false);
    }

    private String getRomanLife(int life) {
        // 限制最大值为10
        life = Math.min(life, 10);

        // 转换为罗马数字
        switch(life) {
            case 0: return "-";
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            case 8: return "VIII";
            case 9: return "IX";
            case 10: return "X";
            default: return String.valueOf(life);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 绘制背景
        controller.getBackground().painting(g);

        // 绘制所有游戏实体
        drawGameEntities(g);

        // 绘制UI元素
        drawUI(g);

        // 绘制游戏状态图片
        drawGameState(g);
    }

    private void drawGameEntities(Graphics g) {
        // 绘制英雄机
        controller.getHero().painting(g);

        // 绘制敌机
        for (FlyingObject enemy : controller.getEnemies()) {
            enemy.painting(g);
        }

        // 绘制子弹
        for (FlyingObject bullet : controller.getBullets()) {
            bullet.painting(g);
        }
    }

    private void drawUI(Graphics g) {
        // 生命使用大字体（28号）
        g.setFont(new Font("Calibri", Font.BOLD, 28)); // 修改点1
        FontMetrics lifeMetrics = g.getFontMetrics();
        // 绘制生命（在下方）
        ResourceManager.LIFE_ICON.paintIcon(this, g, LIFE_X, LIFE_Y);
        int lifeIconCenterY = LIFE_Y + ResourceManager.LIFE_ICON.getIconHeight()/2;
        int lifeTextBaseline = lifeIconCenterY + (lifeMetrics.getAscent() - lifeMetrics.getDescent())/2;
        g.setColor(Color.WHITE);

        // 使用罗马数字显示生命值
        String romanLife = getRomanLife(controller.getLife());
        g.drawString(" " + romanLife,
                LIFE_X + VALUE_OFFSET_X,
                lifeTextBaseline);

        // 积分使用小字体（24号）
        g.setFont(new Font("Calibri", Font.BOLD, 24)); // 修改点2
        FontMetrics scoreMetrics = g.getFontMetrics();

        // 绘制积分（在上方）
        ResourceManager.SCORE_ICON.paintIcon(this, g, SCORE_X, SCORE_Y);
        int scoreIconCenterY = SCORE_Y + ResourceManager.SCORE_ICON.getIconHeight()/2;
        int scoreTextBaseline = scoreIconCenterY + (scoreMetrics.getAscent() - scoreMetrics.getDescent())/2;
        g.setColor(Color.WHITE);
        g.drawString("× " + controller.getScore(),
                SCORE_X + 70,
                scoreTextBaseline);

        // 绘制技能UI保持不变
        drawSkillsUI(g);
    }

    private void drawSkillsUI(Graphics g) {
        int startX = SKILL_X;
        int startY = SKILL_Y;

        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", Font.BOLD, 20)); // 修改点3
        // 技能1：无敌
        ResourceManager.SKILL1_ICON.paintIcon(this, g, startX, startY);
        String skill1Text = "Cost:" + controller.getSkill1Cost();
        int textWidth1 = g.getFontMetrics().stringWidth(skill1Text);
        int textX1 = startX + (ResourceManager.SKILL1_ICON.getIconWidth() - textWidth1) / 2;
        g.drawString(skill1Text, textX1, startY + TEXT_OFFSET_Y);

        // 技能2：火力
        ResourceManager.SKILL2_ICON.paintIcon(this, g, startX + SKILL_SPACING, startY);
        String skill2Text = "Cost:" + controller.getSkill2Cost();
        int textWidth2 = g.getFontMetrics().stringWidth(skill2Text);
        int textX2 = startX + SKILL_SPACING + (ResourceManager.SKILL2_ICON.getIconWidth() - textWidth2) / 2;
        g.drawString(skill2Text, textX2, startY + TEXT_OFFSET_Y);
    }

    private void drawGameState(Graphics g) {
        int centerX = (SCREEN_WIDTH - ResourceManager.START.getIconWidth()) / 2;
        int centerY = (SCREEN_HEIGHT - ResourceManager.START.getIconHeight()) / 2;

        switch (controller.getGameState()) {
            case GameController.READY:
                ResourceManager.START.paintIcon(this, g, centerX, centerY);
                break;
            case GameController.PAUSE:
                ResourceManager.PAUSE.paintIcon(this, g, centerX, centerY);
                break;
            case GameController.GAMEOVER:
                ResourceManager.GAMEOVER.paintIcon(this, g, centerX, centerY);
                break;
        }
    }
}