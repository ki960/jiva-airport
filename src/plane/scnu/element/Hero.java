package plane.scnu.element;
import plane.scnu.controller.GameController;
import java.awt.*;
import java.awt.geom.Point2D;

public class Hero extends FlyingObject {
    private static final float[] GRADIENT_DISTANCES = {0.0f, 0.7f, 1.0f};
    private static final RenderingHints RENDERING_HINTS = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
    );

    private int fireCounter = 0;
    private boolean invincible = false;
    private int invincibleTime = 0;
    private boolean tripleFire = false;
    private int tripleFireTime = 0;
    private int doubleTime = 0;
    private int tripleFireGlowRadius = 0;
    private float tripleFireGlowAlpha = 0.0f;
    private int doubleFireGlowRadius = 0;
    private float doubleFireGlowAlpha = 0.0f;
    private int effectCounter = 0;

    public boolean isInvincible() {
        return invincible;
    }

    public boolean isTripleFire() {
        return tripleFire;
    }

    public boolean isDoubleFire() {
        return doubleTime > 0;
    }

    public void activateInvincible(int duration) {
        invincible = true;
        invincibleTime = duration;
    }

    public void activateTripleFire(int duration) {
        tripleFire = true;
        tripleFireTime = 2 * duration;
        tripleFireGlowRadius = 40;
        tripleFireGlowAlpha = 0.7f;
    }

    private void updateGlowEffects() {
        effectCounter++;
        if (tripleFire) {
            tripleFireGlowRadius = 50 + (int)(10 * Math.sin(effectCounter * 0.06));
            tripleFireGlowAlpha = 0.6f + 0.1f * (float)Math.sin(effectCounter * 0.08);
        }
        if (isDoubleFire()) {
            doubleFireGlowRadius = 40 + (int)(10 * Math.sin(effectCounter * 0.12));
            doubleFireGlowAlpha = 0.5f + 0.1f * (float)Math.sin(effectCounter * 0.1);
        }
    }

    private Graphics2D createOptimizedGraphics(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHints(RENDERING_HINTS);
        return g2d;
    }

    private Point2D calculateEffectCenter(double xOffset) {
        return new Point2D.Float(
                (float)(x + width / 2 + xOffset),
                (float)(y + height / 2)
        );
    }

    private void paintRadialGlow(Graphics g, Point2D center, int radius,
                                 Color[] colors, float alphaModifier) {
        Graphics2D g2d = createOptimizedGraphics(g);
        try {
            for (int i = 0; i < colors.length; i++) {
                Color original = colors[i];
                int alpha = (int)(original.getAlpha() * alphaModifier);
                colors[i] = new Color(original.getRed(), original.getGreen(),
                        original.getBlue(), alpha);
            }

            RadialGradientPaint rgp = new RadialGradientPaint(
                    center, radius, GRADIENT_DISTANCES, colors
            );
            g2d.setPaint(rgp);
            g2d.fillOval(
                    (int)(center.getX() - radius),
                    (int)(center.getY() - radius),
                    radius * 2,
                    radius * 2
            );
        } finally {
            g2d.dispose();
        }
    }

    private void paintTripleFireGlow(Graphics g) {
        Point2D center = calculateEffectCenter(-10);
        Color[] colors = {
                new Color(255, 80, 40, 220),
                new Color(220, 40, 20, 180),
                new Color(150, 20, 10, 80)
        };
        paintRadialGlow(g, center, tripleFireGlowRadius, colors, tripleFireGlowAlpha);
    }

    private void paintDoubleFireGlow(Graphics g) {
        Point2D center = calculateEffectCenter(-10);
        Color[] colors = {
                new Color(255, 230, 100, 220),
                new Color(240, 190, 40, 180),
                new Color(200, 150, 20, 80)
        };
        paintRadialGlow(g, center, doubleFireGlowRadius, colors, doubleFireGlowAlpha);
    }

    @Override
    public void nextImage() {
        super.nextImage();
        updateGlowEffects();
    }

    @Override
    public void painting(Graphics g) {
        paintGlowEffects(g);
        super.painting(g);
        if (invincible) paintInvincibleEffect(g);
    }

    private void paintGlowEffects(Graphics g) {
        if (tripleFire) paintTripleFireGlow(g);
        if (isDoubleFire()) paintDoubleFireGlow(g);
    }

    private void paintInvincibleEffect(Graphics g) {
        Graphics2D g2d = createOptimizedGraphics(g);
        try {
            double breathFactor = 0.9 + 0.1 * Math.sin(effectCounter * 0.03);
            float alphaFactor = 0.65f + 0.15f * (float)Math.abs(Math.sin(effectCounter * 0.03));
            int baseRadius = (int)(Math.max(width, height) * 0.6);
            Point2D center = calculateEffectCenter(0);

            Color[] colors = {
                    new Color(100, 180, 255, (int)(220 * alphaFactor)),
                    new Color(64, 156, 255, (int)(150 * alphaFactor)),
                    new Color(30, 100, 200, 0)
            };

            RadialGradientPaint rgp = new RadialGradientPaint(
                    center, (float)(baseRadius * breathFactor),
                    GRADIENT_DISTANCES, colors
            );
            g2d.setPaint(rgp);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            int diameter = (int)(baseRadius * 2 * breathFactor);
            int xPos = (int)(center.getX() - baseRadius * breathFactor);
            int yPos = (int)(center.getY() - baseRadius * breathFactor);
            g2d.fillOval(xPos, yPos, diameter, diameter);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f * alphaFactor));
            g2d.setColor(new Color(120, 210, 255, 100));
            g2d.setStroke(new BasicStroke(3f));
            g2d.drawOval(xPos, yPos, diameter, diameter);
        } finally {
            g2d.dispose();
        }
    }

    public void updateSkills() {
        if (invincibleTime > 0) {
            invincibleTime--;
            if (invincibleTime == 0) invincible = false;
        }
        if (tripleFireTime > 0) {
            tripleFireTime--;
            if (tripleFireTime == 0) {
                tripleFire = false;
                tripleFireGlowAlpha = 0.0f;
            }
        }
        if (doubleTime > 0) {
            doubleTime--;
            if (doubleTime == 0) doubleFireGlowAlpha = 0.0f;
        }
    }

    public void getDoubleFire() {
        doubleTime = 280;
        doubleFireGlowRadius = 30;
        doubleFireGlowAlpha = 0.5f;
    }

    public void updateFireState() {}

    public boolean canFire() {
        fireCounter++;
        int baseInterval = 17;
        int actualInterval = doubleTime > 0 ? (int)(baseInterval * 2.0/3) : baseInterval;
        if (fireCounter >= actualInterval) {
            fireCounter = 0;
            return true;
        }
        return false;
    }

    public Bullet[] fireTriple() {
        double centerX = this.x + this.width;
        double centerY = this.y + this.height / 2;
        return new Bullet[] {
                new Bullet(centerX, centerY - 20),
                new Bullet(centerX, centerY),
                new Bullet(centerX, centerY + 20)
        };
    }

    public Bullet fire() {
        double centerX = this.x + this.width;
        double centerY = this.y + this.height / 2;
        return new Bullet(centerX, centerY);
    }

    @Override
    public void move() {
        super.move();
    }

    public void move(int mouseX, int mouseY) {
        this.x = mouseX - width / 2;
        this.y = mouseY - height / 2;
        if (x < 0) x = 0;
        if (x > GameController.SCREEN_WIDTH - width) x = GameController.SCREEN_WIDTH - width;
        if (y < 0) y = 0;
        if (y > GameController.SCREEN_HEIGHT - height) y = GameController.SCREEN_HEIGHT - height;
    }
}