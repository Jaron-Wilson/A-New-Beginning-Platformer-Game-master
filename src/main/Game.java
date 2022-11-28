package main;

import entities.Player;
import levels.LevelManager;

import java.awt.*;

public class Game implements Runnable{

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private LevelManager levelManager;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    public final static int TILES_DEFAULT_SIZE = 32;
//    SCALE OF GAME
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;


    private Player player;
    public Game() {
        initClasses();

        System.out.println("Game Class Starting...");
        gamePanel = new GamePanel(this);
//        Create Game Window
        gameWindow = new GameWindow(gamePanel);
//        gets input focus
        gamePanel.requestFocus();

        startGameLoop();

    }

    private void initClasses() {
        levelManager = new LevelManager(this);
        player = new Player(200, 200, (int) (64 * SCALE), (int) (40 * SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update(){
        player.update();
        levelManager.update();
    }

    public void render(Graphics g) {
        levelManager.draw(g);
        player.render(g);
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();

        int frames = 0, updates = 0;

        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
//        FRAMES
        double deltaF = 0;

        while (true) {

            long currentTime = System.nanoTime();

            deltaU += (currentTime-previousTime)/ timePerUpdate;
            deltaF += (currentTime-previousTime)/ timePerFrame;
            previousTime = currentTime;
            if (deltaU >= 1) {
//                UPDATE
                update();
                updates++;
                deltaU--;
            }


            if (deltaF >= 1) {
//               then repaint
                gamePanel.repaint();
                frames++;
                deltaF--;

            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }

        }
    }


    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }
}
