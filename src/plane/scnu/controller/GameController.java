package plane.scnu.controller;

import plane.scnu.element.*;
import plane.scnu.manager.Award;
import plane.scnu.manager.Enemy;
import plane.scnu.manager.ObjectFactory;
import plane.scnu.show.GameView;

import java.awt.event.*;
import java.util.*;

public class GameController {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int READY = 0;
    public static final int RUNNING = 1;
    public static final int PAUSE = 2;
    public static final int GAMEOVER = 3;

    private Background background;
    private Hero hero;
    private List<EnemyPlane> enemies;
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

    public GameController() {
        initGame();
        this.view = new GameView(this);
    }

    public void initGame() {
        background = ObjectFactory.createBackground();
        hero = ObjectFactory.createHero();
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
                if (gameState == RUNNING) hero.move(e.getX(), e.getY());
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
        } else if (gameState == GAMEOVER) {
            initGame();
            gameState = RUNNING;
        }
    }

    private void activateSkill1() {
        if (score >= skill1Cost && !hero.isInvincible()) {
            skillActive = true;
            score -= skill1Cost;
            hero.activateInvincible(280);
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
        if (score >= skill2Cost && !hero.isTripleFire()) {
            skillActive = true;
            score -= skill2Cost;
            hero.activateTripleFire(280);
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
        if (index % 12 != 0) return;

        Random rand = new Random();
        if (rand.nextDouble() < 0.7) {
            int type = rand.nextInt(10);
            EnemyPlane plane = ObjectFactory.createEnemy(type);
            plane.setPosition(SCREEN_WIDTH, rand.nextInt(SCREEN_HEIGHT - (int) plane.getHeight()));
            switch (plane) {
                case Airplane a -> a.setStep(rand.nextDouble() * 3.0 + 2.0);
                case BigAirplane b -> b.setStep(rand.nextDouble() * 0.9 + 0.8);
                case Gift g -> g.setStep(rand.nextDouble() * 1.7 + 1.3);
                default -> {}
            }
            enemies.add(plane);
        }
    }

    public void createBullet() {
        if (!hero.isLiving()) return;

        hero.updateSkills();
        hero.updateFireState();
        if (hero.canFire()) {
            if (hero.isTripleFire()) {
                Bullet[] tripleBullets = hero.fireTriple();
                if (tripleBullets != null) {
                    Collections.addAll(bullets, tripleBullets);
                }
            } else {
                Bullet bullet = hero.fire();
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
        if (hero == null || hero.isInvincible()) return;

        synchronized (enemiesLock) {
            for (EnemyPlane enemy : enemies) {
                if (enemy == null || !enemy.isLiving()) continue;

                if (hero.bong(enemy)) {
                    hero.goDead();
                    enemy.goDead();
                    break;
                }
            }
        }

        if (!hero.isLiving() && hero.isWait()) {
            if (life > 1) {
                respawnHero();
            } else {
                life--;
                gameState = GAMEOVER;
            }
        }
    }

    private void respawnHero() {
        hero = ObjectFactory.createHero();
        synchronized (enemiesLock) {
            for (EnemyPlane enemy : enemies) {
                if (enemy != null) enemy.goDead();
            }
        }
        life--;
    }

    private void checkBulletCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<EnemyPlane> enemiesToRemove = new ArrayList<>();

        synchronized (enemiesLock) {
            synchronized (bulletsLock) {
                for (Bullet bullet : bullets) {
                    if (bullet == null || !bullet.isLiving()) continue;

                    for (EnemyPlane enemy : enemies) {
                        if (enemy == null || !enemy.isLiving()) continue;

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
                hero.getDoubleFire();
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
            for (EnemyPlane enemy : enemies) {
                if (enemy != null) enemy.move();
            }
        }

        synchronized (bulletsLock) {
            for (Bullet bullet : bullets) {
                if (bullet != null) bullet.move();
            }
        }

        if (hero != null) hero.updateSkills();
    }

    public void gameLoop() {
        if (gameState == RUNNING) {
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
    public Hero getHero() { return hero; }
    public List<EnemyPlane> getEnemies() { return enemies; }
    public List<Bullet> getBullets() { return bullets; }
    public int getGameState() { return gameState; }
    public int getLife() { return life; }
    public int getScore() { return score; }
    public int getSkill1Cost() { return skill1Cost; }
    public int getSkill2Cost() { return skill2Cost; }
    public GameView getView() { return view; }
}