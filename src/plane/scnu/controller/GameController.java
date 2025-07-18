package plane.scnu.controller;

import plane.scnu.element.*;
import plane.scnu.manager.*;
import plane.scnu.show.GameView;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 游戏主控制器 - 负责游戏逻辑和状态管理
 */
public class GameController {
    // 游戏状态常量
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int READY = 0;
    public static final int RUNNING = 1;
    public static final int PAUSE = 2;
    public static final int GAMEOVER = 3;

    // 游戏核心组件
    private Background background;
    private Hero hero;
    private List<EnemyPlane> enemies;
    private List<Bullet> bullets;
    private final GameView view;

    // 游戏状态
    private int gameState = READY;
    private int index = 0;

    // 游戏属性
    private int life = 3;
    private int score = 0;
    private int skill1Cost = 400; // 从500调整为400
    private int skill2Cost = 500; // 从700调整为500

    public GameController() {
        initGame();
        this.view = new GameView(this);
    }

    /**
     * 初始化游戏
     */
    public void initGame() {
        // 创建游戏对象
        background = ObjectFactory.createBackground();
        hero = ObjectFactory.createHero();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

        // 设置背景位置
        background.setPosition(0, 0);
        background.setBackgroundPosition();

        // 重置游戏状态
        gameState = READY;
        life = 3;
        score = 0;
        skill1Cost = 400;
        skill2Cost = 500;
        index = 0;
    }

    /**
     * 启动游戏
     */
    public void start() {
        GameTimer timer = new GameTimer(this);
        timer.start();
        setupEventListeners();
    }

    /**
     * 设置事件监听器
     */
    private void setupEventListeners() {
        // 鼠标事件监听
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameState == READY) {
                    gameState = RUNNING;
                } else if (gameState == GAMEOVER) {
                    initGame();
                    gameState = RUNNING;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (gameState == RUNNING) {
                    gameState = PAUSE;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (gameState == PAUSE) {
                    gameState = RUNNING;
                }
            }
        });

        // 鼠标移动监听
        view.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (gameState == RUNNING) {
                    hero.move(e.getX(), e.getY());
                }
            }
        });

        // 键盘事件监听
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameState == RUNNING) {
                    if (e.getKeyCode() == KeyEvent.VK_Q) {
                        activateSkill1();
                    } else if (e.getKeyCode() == KeyEvent.VK_W) {
                        activateSkill2();
                    }
                }
            }
        });

        view.setFocusable(true);
        view.requestFocusInWindow();
    }

    /**
     * 激活技能1（无敌）
     */
    private void activateSkill1() {
        if (score >= skill1Cost) {
            score -= skill1Cost;
            hero.activateInvincible(280); // 持续时间从250调整为280
            // 新增单独增量
            int skill1Increment = 200;
            skill1Cost += skill1Increment; // 增量从200调整为200
        }
    }

    /**
     * 激活技能2（三倍火力）
     */
    private void activateSkill2() {
        if (score >= skill2Cost) {
            score -= skill2Cost;
            hero.activateTripleFire(280); // 持续时间从250调整为280
            // 新增单独增量
            int skill2Increment = 250;
            skill2Cost += skill2Increment; // 增量从200调整为250
        }
    }

    /**
     * 生成敌机
     */
    public void createPlane() {
        Random rand = new Random();
        // 刷怪间隔从20帧调整为12帧
        if (index % 12 == 0 && rand.nextDouble() < 0.7) {
            int type = rand.nextInt(10);
            EnemyPlane plane = ObjectFactory.createEnemy(type);
            plane.setPosition(SCREEN_WIDTH, rand.nextInt(SCREEN_HEIGHT - (int)plane.getHeight()));
            // 设置不同类型敌机的速度
            if (plane instanceof Airplane) {
                plane.setStep(rand.nextDouble() * 3.0 + 2.0); // 小敌机速度2.0-5.0
            } else if (plane instanceof BigAirplane) {
                plane.setStep(rand.nextDouble() * 0.9 + 0.8); // 大敌机速度0.8-1.7
            } else if (plane instanceof Gift) {
                plane.setStep(rand.nextDouble() * 1.7 + 1.3); // 蜜蜂速度1.3-3.0
            }
            enemies.add(plane);
        }
    }

    /**
     * 生成子弹
     */
    public void createBullet() {
        if (!hero.isLiving()) return;

        hero.updateSkills();
        hero.updateFireState();

        if (hero.canFire()) {
            if (hero.isTripleFire()) {
                Bullet[] tripleBullets = hero.fireTriple();
                Collections.addAll(bullets, tripleBullets);
            } else {
                Bullet bullet = hero.fire();
                bullets.add(bullet);
            }
        }
    }

    /**
     * 碰撞检测
     */
    public void checkCollisions() {
        checkHeroCollision();
        checkBulletCollisions();
    }

    /**
     * 英雄机碰撞检测
     */
    private void checkHeroCollision() {
        // 无敌状态不检测碰撞
        if (hero.isInvincible()) return;

        if (hero.isLiving()) {
            for (EnemyPlane enemy : enemies) {
                if (!enemy.isLiving()) continue;

                if (hero.bong(enemy)) {
                    hero.goDead();
                    enemy.goDead();
                    break;
                }
            }
        } else if (hero.isWait()) {
            if (life > 1) {
                // 复活英雄
                hero = ObjectFactory.createHero();
                // 清除所有敌机
                for (EnemyPlane enemy : enemies) {
                    enemy.goDead();
                }
                life--;
            } else {
                life--;
                gameState = GAMEOVER;
            }
        }
    }

    /**
     * 子弹碰撞检测
     */
    private void checkBulletCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<EnemyPlane> enemiesToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (!bullet.isLiving()) continue;

            for (EnemyPlane enemy : enemies) {
                if (!enemy.isLiving()) continue;

                if (enemy.bong(bullet)) {
                    enemy.attack();
                    bullet.goDead();
                    addScore(enemy);

                    bulletsToRemove.add(bullet);
                    if (enemy.isDead()) {
                        enemiesToRemove.add(enemy);
                    }
                    break;
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);
    }

    /**
     * 添加分数
     */
    private void addScore(FlyingObject object) {
        if (object instanceof Enemy) {
            score += ((Enemy) object).getScore();
        }

        if (object instanceof Award) {
            int awardType = ((Award) object).getAward();
            if (awardType == Award.DOUBLE_FIRE) {
                hero.getDoubleFire();
            } else if (awardType == Award.LIFE) {
                life++;
            }
        }
    }

    /**
     * 清理对象
     */
    public void cleanObjects() {
        // 清理敌机
        List<EnemyPlane> enemiesToRemove = new ArrayList<>();
        for (EnemyPlane enemy : enemies) {
            if (enemy.isWait() || enemy.outOfBound()) {
                enemiesToRemove.add(enemy);
            }
        }
        enemies.removeAll(enemiesToRemove);

        // 清理子弹
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (bullet.isDead() || bullet.outOfBound()) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);
    }

    /**
     * 更新游戏状态
     */
    public void updateGameState() {
        index++;

        // 更新背景
        background.move();

        // 更新敌机
        for (EnemyPlane enemy : enemies) {
            enemy.move();
        }

        // 更新子弹
        for (Bullet bullet : bullets) {
            bullet.move();
        }

        // 更新英雄技能
        hero.updateSkills();
    }

    /**
     * 游戏循环
     */
    public void gameLoop() {
        if (gameState == RUNNING) {
            createPlane();
            createBullet();
            checkCollisions();
            cleanObjects();
            updateGameState();
        }

        // 请求重绘
        view.repaint();
    }

    // ====== 获取方法 ======
    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public List<EnemyPlane> getEnemies() {
        return enemies;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public int getGameState() {
        return gameState;
    }

    public int getLife() {
        return life;
    }

    public int getScore() {
        return score;
    }

    public int getSkill1Cost() {
        return skill1Cost;
    }

    public int getSkill2Cost() {
        return skill2Cost;
    }

    public GameView getView() {
        return view;
    }
}