package mission.controller;

import mission.element.*;
import mission.manager.Award;
import mission.manager.Enemy;
import mission.manager.ObjectFactory;
import mission.show.GameView;

import java.awt.event.*;
import java.util.*;

public class GameController {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int READY = 0;
    public static final int RUNNING = 1;
    public static final int PAUSE = 2;
    public static final int FAILED = 3;
    public static final int WIN = 4;

    private Background background;
    private Witch witch;
    private List<Monster> enemies;
    private List<Bullet> bullets;
    private final GameView view;
    private final Object enemiesLock = new Object();
    private final Object bulletsLock = new Object();

    private int gameState = READY;
    private int index = 0;
    private int life = 3;
    private int score = 0;
    private int skill1Cost = 400;
    private int skill2Cost = 500;
    private boolean skillActive = false;
    private float gameTime = 0;
    private static final float WIN_TIME = 60f;
    private int timeHintType = 0;
    private int hintDisplayTime = 0;

    public GameController() {
        initGame();
        this.view = new GameView(this);
    }

    public void initGame() {
        background = ObjectFactory.createBackground();
        witch = ObjectFactory.createHero();
        enemies = Collections.synchronizedList(new ArrayList<>());
        bullets = Collections.synchronizedList(new ArrayList<>());
        background.setPosition(0, 0);
        background.setBackgroundPosition();
        gameState = READY;
        life = 3;
        score = 0;
        skill1Cost = 400;
        skill2Cost = 500;
        index = 0;
        skillActive = false;
        gameTime = 0;
        timeHintType = 0;
        hintDisplayTime = 0;
    }

    public void start() {
        GameTimer timer = new GameTimer(this);
        timer.start();
        setupEventListeners();
    }

    private void setupEventListeners() {
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (gameState == RUNNING) gameState = PAUSE;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (gameState == PAUSE) gameState = RUNNING;
            }
        });

        view.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (gameState == RUNNING) witch.move(e.getX(), e.getY());
            }
        });

        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameState == RUNNING && !skillActive) {
                    if (e.getKeyCode() == KeyEvent.VK_Q) activateSkill1();
                    else if (e.getKeyCode() == KeyEvent.VK_W) activateSkill2();
                }
            }
        });
        view.setFocusable(true);
        view.requestFocusInWindow();
    }

    private void handleMouseClick() {
        if (gameState == READY) {
            gameState = RUNNING;
        } else if (gameState == FAILED || gameState == WIN) {
            initGame();
            gameState = RUNNING;
        }
    }

    private void activateSkill1() {
        if (score >= skill1Cost && !witch.isInvincible()) {
            skillActive = true;
            score -= skill1Cost;
            witch.activateInvincible(280);
            skill1Cost += 200;
            view.markSkillUsed(1);
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    skillActive = false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    private void activateSkill2() {
        if (score >= skill2Cost && !witch.isTripleFire()) {
            skillActive = true;
            score -= skill2Cost;
            witch.activateTripleFire(280);
            skill2Cost += 250;
            view.markSkillUsed(2);
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    skillActive = false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    public void createPlane() {
        double spawnProbability = Math.min(0.85, 0.3 + (gameTime * 0.012));
        int spawnInterval = Math.max(4, 12 - (int)(gameTime * 0.15));
        double speedMultiplier = 1.0 + (gameTime * 0.008);

        if (index % spawnInterval != 0) return;

        Random rand = new Random();
        if (rand.nextDouble() < spawnProbability) {
            int type = rand.nextInt(12);
            if (gameTime > 40 && rand.nextDouble() < 0.45) {
                type = Math.max(6, type);
            }

            Monster plane = ObjectFactory.createEnemy(type);
            plane.setPosition(SCREEN_WIDTH, rand.nextInt(SCREEN_HEIGHT - (int) plane.getHeight()));

            switch (plane) {
                case Mob a -> {
                    double baseSpeed = rand.nextDouble() * 3.0 + 2.0;
                    a.setStep(baseSpeed * Math.min(1.4, speedMultiplier));
                }
                case Elite b -> {
                    double baseSpeed = rand.nextDouble() * 0.9 + 0.8;
                    b.setStep(baseSpeed * Math.min(1.2, speedMultiplier * 0.6));
                }
                case Gift g -> g.setStep(rand.nextDouble() * 1.7 + 1.3);
                default -> {}
            }
            enemies.add(plane);
        }
    }

    public void createBullet() {
        if (witch.isLiving()) return;

        witch.updateSkills();
        witch.updateFireState();
        if (witch.canFire()) {
            if (witch.isTripleFire()) {
                Bullet[] tripleBullets = witch.fireTriple();
                if (tripleBullets != null) {
                    Collections.addAll(bullets, tripleBullets);
                }
            } else {
                Bullet bullet = witch.fire();
                if (bullet != null) {
                    bullets.add(bullet);
                }
            }
        }
    }

    public void checkCollisions() {
        checkHeroCollision();
        checkBulletCollisions();
    }

    private void checkHeroCollision() {
        if (witch == null || witch.isInvincible()) return;

        synchronized (enemiesLock) {
            for (Monster enemy : enemies) {
                if (enemy == null || enemy.isLiving()) continue;

                if (witch.bong(enemy)) {
                    witch.goDead();
                    enemy.goDead();
                    break;
                }
            }
        }

        if (witch.isLiving() && witch.isWait()) {
            if (life > 1) {
                respawnHero();
            } else {
                life--;
                gameState = FAILED;
            }
        }
    }

    private void respawnHero() {
        witch = ObjectFactory.createHero();
        synchronized (enemiesLock) {
            for (Monster enemy : enemies) {
                if (enemy != null) enemy.goDead();
            }
        }
        life--;
    }

    private void checkBulletCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Monster> enemiesToRemove = new ArrayList<>();

        synchronized (enemiesLock) {
            synchronized (bulletsLock) {
                for (Bullet bullet : bullets) {
                    if (bullet == null || bullet.isLiving()) continue;

                    for (Monster enemy : enemies) {
                        if (enemy == null || enemy.isLiving()) continue;

                        if (enemy.bong(bullet)) {
                            enemy.attack();
                            bullet.goDead();

                            if (enemy.isDead()) {
                                addScore(enemy);
                                enemiesToRemove.add(enemy);
                            }
                            bulletsToRemove.add(bullet);
                            break;
                        }
                    }
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);
    }

    private void addScore(FlyingObject object) {
        if (object == null || !object.isDead()) return;

        if (object instanceof Enemy) {
            score += ((Enemy) object).getScore();
        }
        if (object instanceof Award) {
            int awardType = ((Award) object).getAward();
            if (awardType == Award.DOUBLE_FIRE) {
                witch.getDoubleFire();
            } else if (awardType == Award.LIFE && life < 10) {
                life++;
            }
        }
    }

    public void cleanObjects() {
        synchronized (enemiesLock) {
            enemies.removeIf(enemy -> enemy == null || enemy.isWait() || enemy.outOfBound());
        }

        synchronized (bulletsLock) {
            bullets.removeIf(bullet -> bullet == null || bullet.isDead() || bullet.outOfBound());
        }
    }

    public void updateGameState() {
        index++;
        if (background != null) background.move();

        synchronized (enemiesLock) {
            for (Monster enemy : enemies) {
                if (enemy != null) enemy.move();
            }
        }

        synchronized (bulletsLock) {
            for (Bullet bullet : bullets) {
                if (bullet != null) bullet.move();
            }
        }

        if (witch != null) witch.updateSkills();
    }

    public void gameLoop() {
        if (gameState == RUNNING) {
            gameTime += 1f / 60;
            if (Math.abs(gameTime - 30f) < 0.1f) {
                timeHintType = 1;
                hintDisplayTime = 60;
            } else if (Math.abs(gameTime - 50f) < 0.1f) {
                timeHintType = 2;
                hintDisplayTime = 60;
            }

            if (hintDisplayTime > 0) {
                hintDisplayTime--;
                if (hintDisplayTime == 0) {
                    timeHintType = 0;
                }
            }
            if (gameTime >= WIN_TIME) {
                gameState = WIN;
            }
            try {
                createPlane();
                createBullet();
                checkCollisions();
                cleanObjects();
                updateGameState();
            } catch (ConcurrentModificationException e) {
                System.err.println("Concurrent modification handled: " + e.getMessage());
            }
        }
        view.repaint();
    }

    public Background getBackground() { return background; }
    public Witch getHero() { return witch; }
    public List<Monster> getEnemies() { return enemies; }
    public List<Bullet> getBullets() { return bullets; }
    public int getGameState() { return gameState; }
    public int getLife() { return life; }
    public int getScore() { return score; }
    public int getSkill1Cost() { return skill1Cost; }
    public int getSkill2Cost() { return skill2Cost; }
    public GameView getView() { return view; }
    public int getTimeHintType() {
        return timeHintType;
    }
    public int getHintDisplayTime() {
        return hintDisplayTime;
    }
}