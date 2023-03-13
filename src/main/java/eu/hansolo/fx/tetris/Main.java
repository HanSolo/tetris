package eu.hansolo.fx.tetris;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


public class Main extends Application {
    protected enum GameMode {
        STANDARD(Color.rgb(0, 0, 0), Color.rgb(18, 18,18)),
        GITHUB(Color.rgb(15, 18, 23), Color.rgb(22, 27, 34));

        public final Color backgroundColor;
        public final Color patternColor;

        GameMode(final Color backgroundColor, final Color patternColor) {
            this.backgroundColor = backgroundColor;
            this.patternColor    = patternColor;
        }
    }
    protected enum Direction {
        UP, RIGHT, DOWN, LEFT;
    }
    protected enum BlockType {
        BLUE(1, new Integer[][] { { 0, 1 },
                                        { 0, 1 },
                                        { 1, 1 }},
                      new Integer[][] { { 1, 1, 1 },
                                        { 0, 0, 1 }},
                      new Integer[][] { { 1, 1 },
                                        { 1, 0 },
                                        { 1, 0 }},
                      new Integer[][] { { 1, 0, 0 },
                                        { 1, 1, 1 }}
             ),
        CYAN(2, new Integer[][] { { 1 },
                                        { 1 },
                                        { 1 },
                                        { 1 } },
                      new Integer[][] { { 1, 1, 1, 1 } },
                      new Integer[][] { { 1 },
                                        { 1 },
                                        { 1 },
                                        { 1 } },
                      new Integer[][] { { 1, 1, 1, 1 } }
             ),
        GREEN(3, new Integer[][] { { 0, 1, 1 },
                                         { 1, 1, 0 } },
                       new Integer[][] { { 1, 0 },
                                         { 1, 1 },
                                         { 0, 1 } },
                       new Integer[][] { { 0, 1, 1 },
                                         { 1, 1, 0 } },
                       new Integer[][] { { 1, 0 },
                                         { 1, 1 },
                                         { 0, 1 } }
              ),
        YELLOW(4, new Integer[][] { { 1, 1 },
                                          { 1, 1 } },
                        new Integer[][] { { 1, 1 },
                                          { 1, 1 } },
                        new Integer[][] { { 1, 1 },
                                          { 1, 1 } },
                        new Integer[][] { { 1, 1 },
                                          { 1, 1 } }),
        ORANGE(5, new Integer[][] { { 1, 0 },
                                          { 1, 0 },
                                          { 1, 1 } },
                        new Integer[][] { { 0, 0, 1 },
                                          { 1, 1, 1 } },
                        new Integer[][] { { 1, 1 },
                                          { 0, 1 },
                                          { 0, 1 } },
                        new Integer[][] { { 1, 1, 1 },
                                          { 1, 0, 0 } }),
        PURPLE(6, new Integer[][] { { 1, 1, 1 },
                                          { 0, 1, 1 } },
                        new Integer[][] { { 1, 1 },
                                          { 1, 1 },
                                          { 1, 0 }},
                        new Integer[][] { { 1, 1, 0 },
                                          { 1, 1, 1 } },
                        new Integer[][] { { 0, 1 },
                                          { 1, 1 },
                                          { 1, 1 } }),
        RED(7, new Integer[][] { { 1, 1, 0 },
                                       { 0, 1, 1 } },
                     new Integer[][] { { 0, 1 },
                                       { 1, 1 },
                                       { 1, 0 } },
                     new Integer[][] { { 1, 1, 0 },
                                       { 0, 1, 1 } },
                     new Integer[][] { { 0, 1 },
                                       { 1, 1 },
                                       { 1, 0 } }
               );

        public final int         code;
        public final Integer[][] matrix_0;
        public final Integer[][] matrix_90;
        public final Integer[][] matrix_180;
        public final Integer[][] matrix_270;



        BlockType(final int code, final Integer[][] matrix_0, final Integer[][] matrix_90, final Integer[][] matrix_180, final Integer[][] matrix_270) {
            this.code       = code;
            this.matrix_0   = matrix_0;
            this.matrix_90  = matrix_90;
            this.matrix_180 = matrix_180;
            this.matrix_270 = matrix_270;
        }
    }

    private static final int                   MATRIX_WIDTH    = 10;
    private static final int                   MATRIX_HEIGHT   = 20;
    private static final double                CELL_WIDTH      = 24;
    private static final double                CELL_HEIGHT     = 24;
    private static final double                GAME_WIDTH      = MATRIX_WIDTH * CELL_WIDTH;
    private static final double                GAME_HEIGHT     = MATRIX_HEIGHT * CELL_HEIGHT;
    private static final double                WIDTH           = GAME_WIDTH + 50;
    private static final double                HEIGHT          = GAME_HEIGHT + 50;
    private static final Random                RND             = new Random();
    private static final Integer[][]           MATRIX          = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                                                                   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
                                                                 };
    private              boolean               running;
    private              long                  lastUpdateCheck;
    private              AnimationTimer        timer;
    private              Canvas                bkgCanvas;
    private              GraphicsContext       bkgCtx;
    private              Canvas                canvas;
    private              GraphicsContext       ctx;
    private              Image                 cyanBlockImg;
    private              Image                 blueBlockImg;
    private              Image                 orangeBlockImg;
    private              Image                 yellowBlockImg;
    private              Image                 greenBlockImg;
    private              Image                 purpleBlockImg;
    private              Image                 redBlockImg;
    private              Image                 githubDarkGreenBlockImg;
    private              Image                 githubGreenBlockImg;
    private              Image                 githubLightGreenBlockImg;
    private              Image                 githubVeryLightGreenBlockImg;
    private              GameMode              gameMode;
    private              int                   level;
    private              Block                 activeBlock;
    private              Block                 nextBlock;
    private              long                  highscore;
    private              long                  score;
    private              int                   noOfLifes;
    private              List<Integer>         rowsToRemove;
    private              Map<BlockType, Image> imageMap;


    // ******************** Methods *******************************************
    @Override public void init() {
        running         = true;
        highscore       = PropertyManager.INSTANCE.getLong(Constants.HIGHSCORE_KEY, 0);
        level           = 1;
        rowsToRemove    = new ArrayList<>();
        imageMap        = new ConcurrentHashMap<>(BlockType.values().length);
        lastUpdateCheck = System.nanoTime();
        timer           = new AnimationTimer() {
            @Override public void handle(final long now) {
                if (running) {
                    // Main loop
                    // update block position
                    if (now > lastUpdateCheck + Constants.LEVEL_SPEED_MAP.get(level)) {
                        if (null == activeBlock) { spawnBlock(); }
                        redraw(true);
                        lastUpdateCheck = now;
                    }
                } else {
                    startScreen();
                }
            }
        };

        // Setup canvas nodes
        bkgCanvas = new Canvas(WIDTH, HEIGHT);
        bkgCtx    = bkgCanvas.getGraphicsContext2D();

        canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        ctx    = canvas.getGraphicsContext2D();

        // Load all images
        loadImages();

        // Load all sounds
        loadSounds();

        // Set Game Mode
        setGameMode(GameMode.STANDARD);

        // Initialize block
        activeBlock = null;
        nextBlock   = new Block(BlockType.values()[RND.nextInt(BlockType.values().length)], MATRIX_WIDTH * 0.5, -CELL_HEIGHT);

        // Initialize level
        noOfLifes  = 3;
        score      = 0;
        setupLevel(level);
    }

    @Override public void start(final Stage stage) {
        final StackPane pane  = new StackPane(bkgCanvas, canvas);
        final Scene     scene = new Scene(pane, WIDTH, HEIGHT);

        scene.setOnKeyPressed(e -> {
            if (running) {
                switch (e.getCode()) {
                    case LEFT  -> activeBlock.moveLeft();
                    case RIGHT -> activeBlock.moveRight();
                    case SPACE -> activeBlock.rotate();
                    case DOWN  -> activeBlock.drop();
                    case M     -> {
                        if (GameMode.STANDARD == gameMode) {
                            setGameMode(GameMode.GITHUB);
                        } else {
                            setGameMode(GameMode.STANDARD);
                        }
                    }
                }
            } else {
                level = 1;
                startLevel(level);
            }
        });

        stage.setTitle("Tetris");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        //playSound(gameStartSnd);

        timer.start();
    }

    @Override public void stop() {
        Platform.exit();
        System.exit(0);
    }


    // Helper methods
    private void loadImages() {
        cyanBlockImg   = new Image(getClass().getResourceAsStream("cyanBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
        blueBlockImg   = new Image(getClass().getResourceAsStream("blueBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
        orangeBlockImg = new Image(getClass().getResourceAsStream("orangeBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
        yellowBlockImg = new Image(getClass().getResourceAsStream("yellowBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
        greenBlockImg  = new Image(getClass().getResourceAsStream("greenBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
        purpleBlockImg = new Image(getClass().getResourceAsStream("purpleBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
        redBlockImg    = new Image(getClass().getResourceAsStream("redBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);

        githubDarkGreenBlockImg      = new Image(getClass().getResourceAsStream("githubDarkGreenBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
        githubGreenBlockImg          = new Image(getClass().getResourceAsStream("githubGreenBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
        githubLightGreenBlockImg     = new Image(getClass().getResourceAsStream("githubLightGreenBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
        githubVeryLightGreenBlockImg = new Image(getClass().getResourceAsStream("githubVeryLightGreenBlock.png"), CELL_WIDTH, CELL_HEIGHT, true, false);
    }

    private void loadSounds() {

    }


    // ******************** Game control **************************************
    // Set mode
    public void setGameMode(final GameMode mode) {
        this.gameMode = mode;
        this.imageMap.clear();
        switch(this.gameMode) {
            case STANDARD -> {
                imageMap.put(BlockType.CYAN, cyanBlockImg);
                imageMap.put(BlockType.BLUE, blueBlockImg);
                imageMap.put(BlockType.ORANGE, orangeBlockImg);
                imageMap.put(BlockType.YELLOW, yellowBlockImg);
                imageMap.put(BlockType.GREEN, greenBlockImg);
                imageMap.put(BlockType.PURPLE, purpleBlockImg);
                imageMap.put(BlockType.RED, redBlockImg);
            }
            case GITHUB   -> {
                imageMap.put(BlockType.CYAN, githubDarkGreenBlockImg);
                imageMap.put(BlockType.BLUE, githubDarkGreenBlockImg);
                imageMap.put(BlockType.ORANGE, githubDarkGreenBlockImg);
                imageMap.put(BlockType.YELLOW, githubGreenBlockImg);
                imageMap.put(BlockType.GREEN, githubGreenBlockImg);
                imageMap.put(BlockType.PURPLE, githubLightGreenBlockImg);
                imageMap.put(BlockType.RED, githubVeryLightGreenBlockImg);
            }
            default       -> {
                imageMap.put(BlockType.CYAN, cyanBlockImg);
                imageMap.put(BlockType.BLUE, blueBlockImg);
                imageMap.put(BlockType.ORANGE, orangeBlockImg);
                imageMap.put(BlockType.YELLOW, yellowBlockImg);
                imageMap.put(BlockType.GREEN, greenBlockImg);
                imageMap.put(BlockType.PURPLE, purpleBlockImg);
                imageMap.put(BlockType.RED, redBlockImg);
            }
        }
        drawBackground();
        redraw(false);
    }


    // Play audio clips
    private void playSound(final AudioClip audioClip) { audioClip.play(); }


    // Spawn block
    private void spawnBlock() {
        activeBlock = nextBlock;
        nextBlock   = new Block(BlockType.values()[RND.nextInt(BlockType.values().length)], MATRIX_WIDTH * 0.5, -CELL_HEIGHT);
    }


    // Start Screen
    private void startScreen() {

    }


    // Start Level
    private void startLevel(final int level) {

    }


    // Game Over
    private void gameOver() {

    }


    // Setup blocks for given level
    private void setupLevel(final int level) {

    }


    // Get angle related block matrix for given block
    private Integer[][] getBlockMatrix(final Block block) {
        switch (block.angle) {
            case 0   -> { return block.blockType.matrix_0; }
            case 90  -> { return block.blockType.matrix_90; }
            case 180 -> { return block.blockType.matrix_180; }
            case 270 -> { return block.blockType.matrix_270; }
            default  -> { return new Integer[0][0]; }
        }
    }


    // Check whether the next move is possible in y direction
    private boolean moveDownAllowed(final Block block) {
        if (!block.active || null == block) { return false; }
        final Integer[][] blockMatrix = getBlockMatrix(block);
        for (int y = 0 ; y < blockMatrix.length ; y++) {
            for (int x = 0; x < blockMatrix[y].length; x++) {
                if (blockMatrix[y][x] == 0) { continue; }
                int matrixX = (int) (block.x + x);
                int matrixY = (int) ((block.y + CELL_HEIGHT) / CELL_HEIGHT) + y;
                if (matrixX < 0 || matrixX > MATRIX_WIDTH - 1) { continue; }
                if (matrixY < 0 || matrixY > MATRIX_HEIGHT - 1) { continue; }
                if (MATRIX[matrixY][matrixX] > 0) { return false; }
            }
        }
        return true;
    }

    private boolean moveLeftAllowed(final Block block) {
        if (!block.active) { return false; }
        final Integer[][] blockMatrix = getBlockMatrix(block);
        for (int y = 0 ; y < blockMatrix.length ; y++) {
            for (int x = 0; x < blockMatrix[y].length; x++) {
                if (blockMatrix[y][x] == 0) { continue; }
                int matrixX = (int) (block.x + x) - 1;
                int matrixY = (int) ((block.y + CELL_HEIGHT) / CELL_HEIGHT) + y - 1;
                if (matrixX < 0 || matrixX > MATRIX_WIDTH - 1) { continue; }
                if (matrixY < 0 || matrixY > MATRIX_HEIGHT - 1) { continue; }
                if (MATRIX[matrixY][matrixX] > 0) { return false; }
            }
        }
        return true;
    }

    private boolean moveRightAllowed(final Block block) {
        if (!block.active) { return false; }
        final Integer[][] blockMatrix = getBlockMatrix(block);
        for (int y = 0 ; y < blockMatrix.length ; y++) {
            for (int x = 0; x < blockMatrix[y].length; x++) {
                if (blockMatrix[y][x] == 0) { continue; }
                int matrixX = (int) (block.x + x) + 1;
                int matrixY = (int) ((block.y + CELL_HEIGHT) / CELL_HEIGHT) + y - 1;
                if (matrixX < 0 || matrixX > MATRIX_WIDTH - 1) { continue; }
                if (matrixY < 0 || matrixY > MATRIX_HEIGHT - 1) { continue; }
                if (MATRIX[matrixY][matrixX] > 0) { return false; }
            }
        }
        return true;
    }

    // Check for complete rows
    private void checkForCompleteRows() {
        for (int i = 0 ; i < MATRIX[0].length ; i++) {
            if (MATRIX[0][i] > 0) {
                noOfLifes--;
                if (noOfLifes < 0) {
                    System.out.println("Game Over");
                    return;
                }
                for (int y = MATRIX_HEIGHT - 1 ; y >= 0 ; y--) {
                    for (int x = 0 ; x < MATRIX[y].length ; x++) {
                        MATRIX[y][x] = 0;
                    }
                }
                return;
            }
        }
        rowsToRemove.clear();
        for (int y = MATRIX_HEIGHT - 1 ; y >= 0 ; y--) {
            int rowSum = 0;
            for (int x = 0 ; x < MATRIX[y].length ; x++) {
                if (MATRIX[y][x] > 0) { rowSum++; }
            }
            if (rowSum == MATRIX_WIDTH) {
                rowsToRemove.add(y);
            }
        }
        // Remove completed rows
        for (int y : rowsToRemove) {
            for (int x = 0 ; x < MATRIX_WIDTH ; x++) { MATRIX[y][x] = 0; }
            for (int row = y - 1 ; row >= 0 ; row--) {
                for (int x = 0 ; x < MATRIX_WIDTH - 1 ; x++) {
                    MATRIX[row + 1][x] = MATRIX[row][x];
                    score += 100;
                }
            }
        }
    }


    // ******************** Redraw ********************************************
    private void drawBackground() {
        bkgCtx.clearRect(0, 0, WIDTH, HEIGHT);
        switch(gameMode) {
            case STANDARD -> {
                bkgCtx.setFill(gameMode.backgroundColor);
                bkgCtx.fillRect(0, 0, WIDTH, HEIGHT);
                bkgCtx.setStroke(Color.GRAY);
                bkgCtx.setLineWidth(10);
                bkgCtx.strokeRoundRect(10, 10, WIDTH - 20, HEIGHT - 20, 10, 10);
            }
            case GITHUB   -> {
                bkgCtx.setFill(gameMode.backgroundColor);
                bkgCtx.fillRect(0, 0, WIDTH, HEIGHT);
                bkgCtx.setStroke(Color.rgb(48, 54, 60));
                bkgCtx.setLineWidth(2);
                bkgCtx.strokeRoundRect(10, 10, WIDTH - 20, HEIGHT - 20, 10, 10);
            }
            default       -> {

            }
        }
    }

    private void redraw(final boolean update) {
        ctx.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        ctx.setFill(gameMode.backgroundColor);
        ctx.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Draw matrix with inactive blocks
        ctx.setFill(gameMode.patternColor);
        for (int y = 0 ; y < MATRIX_HEIGHT ;y++) {
            for (int x = 0; x < MATRIX_WIDTH; x++) {
                switch(gameMode) {
                    case STANDARD -> ctx.fillRect(x * CELL_WIDTH + 1, y * CELL_HEIGHT + 1, 22, 22);
                    case GITHUB   -> ctx.fillRoundRect(x * CELL_WIDTH + 2, y * CELL_HEIGHT + 2, 20, 20, 5, 5);
                }
                switch(MATRIX[y][x]) {
                    case 1 -> ctx.drawImage(imageMap.get(BlockType.BLUE), x * CELL_WIDTH, y * CELL_HEIGHT);
                    case 2 -> ctx.drawImage(imageMap.get(BlockType.CYAN), x * CELL_WIDTH, y * CELL_HEIGHT);
                    case 3 -> ctx.drawImage(imageMap.get(BlockType.GREEN), x * CELL_WIDTH, y * CELL_HEIGHT);
                    case 4 -> ctx.drawImage(imageMap.get(BlockType.YELLOW), x * CELL_WIDTH, y * CELL_HEIGHT);
                    case 5 -> ctx.drawImage(imageMap.get(BlockType.ORANGE), x * CELL_WIDTH, y * CELL_HEIGHT);
                    case 6 -> ctx.drawImage(imageMap.get(BlockType.PURPLE), x * CELL_WIDTH, y * CELL_HEIGHT);
                    case 7 -> ctx.drawImage(imageMap.get(BlockType.RED), x * CELL_WIDTH, y * CELL_HEIGHT);
                }
            }
        }

        // Draw active block
        if (noOfLifes > 0) {
            if (update) { activeBlock.update(); }
            final Integer[][] blockMatrix = getBlockMatrix(activeBlock);
            for (int y = 0 ; y < blockMatrix.length ; y++) {
                for (int x = 0 ; x < blockMatrix[y].length ; x++) {
                    if (blockMatrix[y][x] == 1) {
                        ctx.drawImage(imageMap.get(activeBlock.blockType), (activeBlock.x * CELL_WIDTH) + (x * CELL_WIDTH), (activeBlock.y) + (y * CELL_HEIGHT));
                    }
                }
            }
        } else {
            //ctx.setFill(TEXT_GRAY);
            //ctx.setTextAlign(TextAlignment.CENTER);
            //ctx.fillText("GAME OVER", WIDTH * 0.5, HEIGHT * 0.75);
        }

        if (null != activeBlock && !activeBlock.active) { activeBlock = null; }
    }


    // ******************** Inner Classes *************************************
    private abstract class Sprite {
        public Image     image;
        public Bounds    bounds;
        public double    x; // Center of Sprite in x-direction
        public double    y; // Center of Sprite in y-direction
        public double    r;
        public double    vX;
        public double    vY;
        public double    vR;
        public double    width;
        public double    height;
        public double    size;
        public double    radius;
        public boolean   toBeRemoved;


        // ******************** Constructors **************************************
        public Sprite() {
            this(null, 0, 0, 0, 0, 0, 0);
        }
        public Sprite(final Image image) {
            this(image, 0, 0, 0, 0, 0, 0);
        }
        public Sprite(final Image image, final double x, final double y) {
            this(image, x, y, 0, 0, 0, 0);
        }
        public Sprite(final Image image, final double x, final double y, final double vX, final double vY) {
            this(image, x, y, 0, vX, vY, 0);
        }
        public Sprite(final Image image, final double x, final double y, final double r, final double vX, final double vY) {
            this(image, x, y, r, vX, vY, 0);
        }
        public Sprite(final Image image, final double x, final double y, final double r, final double vX, final double vY, final double vR) {
            this.image       = image;
            this.x           = x;
            this.y           = y;
            this.r           = r;
            this.vX          = vX;
            this.vY          = vY;
            this.vR          = vR;
            this.width       = null == image ? 0 : image.getWidth();
            this.height      = null == image ? 0 : image.getHeight();
            this.size        = this.width > this.height ? width : height;
            this.radius      = this.size * 0.5;
            this.toBeRemoved = false;
            this.bounds      = null == image ? new Bounds(0, 0, 0, 0) : new Bounds(x - image.getWidth() * 0.5, y - image.getHeight() * 0.5, image.getWidth(), image.getHeight());
        }


        // ******************** Methods *******************************************
        protected void init() {}

        public void respawn() {}

        public abstract void update();
    }

    private class Block extends Sprite {
        public BlockType   blockType;
        public int         code;
        public int         angle;
        public boolean     active;


        // ******************** Constructors **************************************
        public Block(final BlockType blockType, final double x, final double y) {
            super(imageMap.get(blockType));
            this.blockType   = blockType;
            this.code        = blockType.code;
            this.x           = x;
            this.y           = y;
            this.vX          = 0;
            this.vY          = 0;
            this.width       = CELL_WIDTH;
            this.height      = CELL_HEIGHT;
            this.angle       = 0;
            this.active      = true;
            this.bounds.set(x, y, width, height);
            init();
        }


        // ******************** Methods *******************************************
        @Override protected void init() {
            size   = width > height ? width : height;
            radius = size * 0.5;
        }

        @Override public void update() {
            if (active) {
                final Integer[][] blockMatrix = getBlockMatrix(Block.this);
                double offsetY = blockMatrix.length * CELL_HEIGHT;
                if (this.y < GAME_HEIGHT - offsetY && moveDownAllowed(Block.this)) {
                    this.y += CELL_HEIGHT;
                } else {
                    // Store inactive blocks in MATRIX
                    for (int y = 0 ; y < blockMatrix.length ; y++) {
                        for (int x = 0 ; x < blockMatrix[y].length ; x++) {
                            int my = (int) (this.y / CELL_HEIGHT + y);
                            if (my > 0 && blockMatrix[y][x] > 0) {
                                MATRIX[(int) (this.y / CELL_HEIGHT + y)][(int) (this.x + x)] = this.code;
                            }
                        }
                    }
                    this.active = false;
                }
            }
            checkForCompleteRows();
        }

        public void moveLeft() {
            if (this.x - 1 < 0 || !moveLeftAllowed(Block.this)) { return; }
            this.x--;
            redraw(false);
        }

        public void moveRight() {
            final Integer[][] blockMatrix = getBlockMatrix(Block.this);
            if (this.x + blockMatrix[0].length > MATRIX_WIDTH - 1 || !moveRightAllowed(Block.this)) { return; }
            this.x++;
            redraw(false);
        }

        public void rotate() {
            final Integer[][] blockMatrix = getBlockMatrix(Block.this);
            if (this.x < 1 || this.x + blockMatrix[0].length - 1 > MATRIX_WIDTH - 2) { return; }
            this.angle += 90;
            if (this.angle >= 360) { this.angle = 0; }
            redraw(false);
        }

        public void drop() {
            int dropCounter = 0;
            while(active) {
                System.out.println(dropCounter);
                redraw(true);
                dropCounter++;
            }
        }
    }

    public class Bounds {
        public double x;
        public double y;
        public double width;
        public double height;
        public double minX;
        public double minY;
        public double maxX;
        public double maxY;
        public double centerX;
        public double centerY;


        // ******************** Constructors **************************************
        public Bounds() {
            this(0, 0, 0, 0);
        }
        public Bounds(final double width, final double height) {
            this(0, 0, width, height);
        }
        public Bounds(final double x, final double y, final double width, final double height) {
            set(x, y, width, height);
        }


        // ******************** Methods *******************************************
        public void set(final Bounds bounds) {
            set(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        public void set(final double x, final double y, final double width, final double height) {
            this.x       = x;
            this.y       = y;
            this.width   = width;
            this.height  = height;
            this.minX    = x;
            this.minY    = y;
            this.maxX    = x + width;
            this.maxY    = y + height;
            this.centerX = x + width * 0.5;
            this.centerY = y + height * 0.5;
        }

        public boolean contains(final double x, final double y) {
            return x >= minX && x <= maxX && y >= minY && y <= maxY;
        }

        public boolean intersects(final Bounds other) {
            return other.minX <= maxX && minX <= other.maxX && other.minY <= maxY && minY <= other.maxY;
        }

        @Override public String toString() {
            return minX + ", " + minY + ", " + maxX + ", " + maxY + ", " + width + ", " + height;
        }
    }


    // ******************** Start *********************************************
    public static void main(String[] args) {
        launch(args);
    }
}