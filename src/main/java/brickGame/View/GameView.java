package brickGame.View;

import brickGame.Controller.GameController;
import brickGame.Model.GameElements.GameBlock;
import brickGame.Model.GameElements.BonusBlock;
import brickGame.Model.GameElements.PenaltyBlock;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * The GameView class represents the graphical user interface for the Brick Breaker game.
 * It handles the initialization and updating of the game elements on the screen.
 *
 * @author Your Name
 * @version 1.0
 */
public class GameView {
    private final GameController controller;
    private Circle ball;
    private Rectangle rect;
    public Pane root;
    private Label scoreLabel;
    private Label heartLabel;
    private Label levelLabel;
    private final int sceneWidth = 500;
    private final int sceneHeigt = 700;
    Button load    = null;
    Button newGame = null;
    /**
     * Constructs a new GameView with the specified GameController.
     *
     * @param controller The GameController associated with this view.
     */
    public GameView(GameController controller) {
        this.controller = controller;
    }
    /**
     * Initializes the view for the game ball.
     */
    public void initBallView() {
        ball = new Circle();
        ball.setRadius(controller.getModel().getBallob().getBallRadius());
        ball.setFill(new ImagePattern(new Image("volleyball.png")));
        ball.setCenterX(controller.getModel().getBallob().getxBall());
        ball.setCenterY(controller.getModel().getBallob().getyBall());
        ball.setStroke(Color.BLACK);
        ball.setStrokeWidth(2);
    }
    /**
     * Initializes the view for the paddle/breaker.
     */
    public void initBreak() {
        rect = new Rectangle();
        rect.setWidth(controller.getModel().getPaddle().getBreakWidth());
        rect.setHeight(controller.getModel().getPaddle().getBreakHeight());
        rect.setX(controller.getModel().getPaddle().getxBreak());
        rect.setY(controller.getModel().getPaddle().getyBreak());
        ImagePattern pattern = new ImagePattern(new Image("break2.png"));
        rect.setFill(pattern);
    }
    /**
     * Initializes the root pane for the game.
     */
    public void initroot() {
        root = new Pane();
    }
    /**
     * Initializes the JavaFX scene for the game.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
    public void initscene(Stage primaryStage) {
        initlabel();
        if (!controller.isLoadFromSave()) {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, newGame, load);
        } else {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        }
        for (GameBlock block :  controller.getModel().getBlocks()) {
            root.getChildren().add(block.getRect());
        }
        Scene scene = new Scene(root, sceneWidth, sceneHeigt);
        scene.getStylesheets().add("style.css");

        primaryStage.setTitle("Brick Breaker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * Initializes the view for the game start menu buttons.
     */
    public void initstartmenubutton() {
        load = new Button("Load Game");
        newGame = new Button("Start Game");
        load.setTranslateX(220);
        load.setTranslateY(370);
        newGame.setTranslateX(220);
        newGame.setTranslateY(330);
    }
    /**
     * Initializes the view for the final level.
     */
    public void initfinallevel() {
        root.getStyleClass().add("finalRoot");
    }
    /**
     * Initializes the labels for score, level, and heart.
     * Sets their initial text values and positions.
     */
    private void initlabel() {
        scoreLabel = new Label("Score: " + controller.getModel().getScore());
        levelLabel = new Label("Level: " + controller.getModel().getLevel());
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + controller.getModel().getHeart());
        heartLabel.setTranslateX(sceneWidth - 115);
        heartLabel.setTranslateY(sceneHeigt - 690);
    }
    /**
     * Handles the view when the ball collides with the floor.
     */
    public void handlefloorcolideView() {
        new GameScore().show((double) sceneWidth / 2, (double) sceneHeigt / 2, -1, this);
    }
    /**
     * Handles the view when hitting a non-normal block.
     *
     * @param block The GameBlock that was hit.
     */
    public void notnormalblockView(GameBlock block) {
        new GameScore().show(block.getX(), block.getY(), 1, this);
        block.getRect().setVisible(false);
    }
    /**
     * Handles the view when hitting a bonus block.
     *
     * @param block The GameBlock that was hit.
     */
    public void handlehitbonusblockView(GameBlock block) {
        final BonusBlock choco = new BonusBlock(block.getRow(), block.getColumn());
        choco.setTimeCreated(controller.getModel().getTime());
        Platform.runLater(() -> {
            root.getChildren().add(choco.getChoco());
            System.out.println("BONUS!");
        });
        controller.getModel().getChocos().add(choco);
    }
    /**
     * Handles the view when hitting a gold block.
     */
    public void handlehitgoldblockView() {
        ball.setStroke(Color.BLACK);
        ball.setStrokeWidth(2);
        System.out.println("goldview");
        ball.setFill(new ImagePattern(new Image("goldball3.png")));
        root.getStyleClass().add("goldRoot");
    }
    /**
     * Handles the view when hitting a bomb block.
     *
     * @param block The GameBlock that was hit.
     */
    public void handlehitbombblockView(GameBlock block){
        final PenaltyBlock bomb = new PenaltyBlock(block.getRow(), block.getColumn());
        Platform.runLater(() -> {
            root.getChildren().add(bomb.getBomb());
            System.out.println("BOMB!");
        });
        controller.getModel().getBombs().add(bomb);
    }
    /**
     * Handles the view when removing the gold effect.
     */
    public void handleremovegoldView() {
        ball.setFill(new ImagePattern(new Image("volleyball.png")));
        root.getStyleClass().remove("goldRoot");
    }
    /**
     * Handles the view when hitting a Choco bonus block.
     *
     * @param choco The BonusBlock that was hit.
     */
    public void handleChocoHitView(BonusBlock choco) {
        Platform.runLater(() -> choco.getChoco().setVisible(false));
        new GameScore().show(choco.getX(), choco.getY(), 3, this);
    }
    /**
     * Handles the view when a bomb hits the paddle.
     *
     * @param bomb The PenaltyBlock (bomb) that hit the paddle.
     */
    public void handleBombPaddleCollisionView(PenaltyBlock bomb) {
        shakeScreen();
        rect.setVisible(false);
        Platform.runLater(() -> root.getChildren().remove(bomb.getBomb()));
        new Timeline(new KeyFrame(
                Duration.seconds(2),
                e -> rect.setVisible(true)
        )).play();
    }
    /**
     * Updates the view elements to reflect the current state of the game.
     */
    public void initView() {
        scoreLabel.setText("Score: " + controller.getModel().getScore());
        heartLabel.setText("Heart : " + controller.getModel().getHeart());
        rect.setX(controller.getModel().getPaddle().getxBreak());
        rect.setY(controller.getModel().getPaddle().getyBreak());
        ball.setCenterX(controller.getModel().getBallob().getxBall());
        ball.setCenterY(controller.getModel().getBallob().getyBall());
    }
    /**
     * Shakes the screen to simulate an impact or effect.
     */
    public void shakeScreen() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), root);
        tt.setByX(20);
        tt.setCycleCount(8);
        tt.setAutoReverse(true);
        tt.setOnFinished(e -> root.setTranslateX(0));
        tt.play();
    }
    /**
     * Displays the "Game Paused" message.
     */
    public void showGamePaused() {
        new GameScore().showGamePaused(this);
    }
    /**
     * Removes the "Game Paused" message from the view.
     */
    public void removeGamePaused() {
        new GameScore().removeGamePaused(this);
    }
    /**
     * Displays the "Game Over" message and restart button.
     */
    public void showGameOver() {
        new GameScore().showGameOver(this);
    }
    /**
     * Displays the "You Win" message and restart button.
     */
    public void showWin() {
        new GameScore().showWin(this);
    }
    /**
     * Displays a custom message on the screen.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {new GameScore().showMessage(message, this);}
    /**
     * Gets the "Load Game" button.
     *
     * @return The "Load Game" button.
     */
    public Button getLoad() {
        return load;
    }
    /**
     * Gets the "Start Game" button.
     *
     * @return The "Start Game" button.
     */
    public Button getNewGame() {
        return newGame;
    }
    /**
     * Sets the visibility of the "Load Game" button.
     *
     * @param tf True to make the button visible, false otherwise.
     */
    public void setLoadvisible(Boolean tf) {
        this.load.setVisible(tf);
    }
    /**
     * Sets the visibility of the "Start Game" button.
     *
     * @param tf True to make the button visible, false otherwise.
     */
    public void setNewGamevisible(Boolean tf) {
        this.newGame.setVisible(tf);
    }
    /**
     * Gets the associated game controller.
     *
     * @return The GameController associated with this view.
     */
    public GameController getController() {
        return controller;
    }
}