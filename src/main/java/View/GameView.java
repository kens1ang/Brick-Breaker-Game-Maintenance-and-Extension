package View;

import Controller.GameController;
import GameElements.GameBlock;
import GameElements.BonusBlock;
import GameElements.PenaltyBlock;
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

    public GameView(GameController controller) {
        this.controller = controller;
    }

    public void initBallView() {
        ball = new Circle();
        ball.setRadius(controller.getModel().getBallob().getBallRadius());
        ball.setFill(new ImagePattern(new Image("volleyball.png")));
        ball.setCenterX(controller.getModel().getBallob().getxBall());
        ball.setCenterY(controller.getModel().getBallob().getyBall());
        ball.setStroke(Color.BLACK);
        ball.setStrokeWidth(2);
    }

    public void initBreak() {
        rect = new Rectangle();
        rect.setWidth(controller.getModel().getPaddle().getBreakWidth());
        rect.setHeight(controller.getModel().getPaddle().getBreakHeight());
        rect.setX(controller.getModel().getPaddle().getxBreak());
        rect.setY(controller.getModel().getPaddle().getyBreak());
        ImagePattern pattern = new ImagePattern(new Image("break2.png"));
        rect.setFill(pattern);
    }

    public void initroot() {
        root = new Pane();
    }

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

    public void initstartmenubutton() {
        load = new Button("Load Game");
        newGame = new Button("Start Game");
        load.setTranslateX(220);
        load.setTranslateY(370);
        newGame.setTranslateX(220);
        newGame.setTranslateY(330);
    }

    public void initfinallevel() {
        root.getStyleClass().add("finalRoot");
    }

    private void initlabel() {
        scoreLabel = new Label("Score: " + controller.getModel().getScore());
        levelLabel = new Label("Level: " + controller.getModel().getLevel());
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + controller.getModel().getHeart());
        heartLabel.setTranslateX(sceneWidth - 115);
        heartLabel.setTranslateY(sceneHeigt - 690);
    }

    public void handlefloorcolideView() {
        new GameScore().show((double) sceneWidth / 2, (double) sceneHeigt / 2, -1, this);
    }

    public void notnormalblockView(GameBlock block) {
        new GameScore().show(block.getX(), block.getY(), 1, this);
        block.getRect().setVisible(false);
    }

    public void handlehitbonusblockView(GameBlock block) {
        final BonusBlock choco = new BonusBlock(block.getRow(), block.getColumn());
        choco.setTimeCreated(controller.getModel().getTime());
        Platform.runLater(() -> {
            root.getChildren().add(choco.getChoco());
            System.out.println("BONUS!");
        });
        controller.getModel().getChocos().add(choco);
    }

    public void handlehitgoldblockView() {
        ball.setStroke(Color.BLACK);
        ball.setStrokeWidth(2);
        ball.setFill(new ImagePattern(new Image("goldball3.png")));
        root.getStyleClass().add("goldRoot");
    }

    public void handlehitbombblockView(GameBlock block){
        final PenaltyBlock bomb = new PenaltyBlock(block.getRow(), block.getColumn());
        Platform.runLater(() -> {
            root.getChildren().add(bomb.getBomb());
            System.out.println("BOMB!");
        });
        controller.getModel().getBombs().add(bomb);
    }

    public void handleremovegoldView() {
        ball.setFill(new ImagePattern(new Image("volleyball.png")));
        root.getStyleClass().remove("goldRoot");
    }

    public void handleChocoHitView(BonusBlock choco) {
        Platform.runLater(() -> choco.getChoco().setVisible(false));
        new GameScore().show(choco.getX(), choco.getY(), 3, this);
    }

    public void handleBombPaddleCollisionView(PenaltyBlock bomb) {
        shakeScreen();
        rect.setVisible(false);
        Platform.runLater(() -> root.getChildren().remove(bomb.getBomb()));
        new Timeline(new KeyFrame(
                Duration.seconds(2),
                e -> rect.setVisible(true)
        )).play();
    }

    public void initView() {
        scoreLabel.setText("Score: " + controller.getModel().getScore());
        heartLabel.setText("Heart : " + controller.getModel().getHeart());
        rect.setX(controller.getModel().getPaddle().getxBreak());
        rect.setY(controller.getModel().getPaddle().getyBreak());
        ball.setCenterX(controller.getModel().getBallob().getxBall());
        ball.setCenterY(controller.getModel().getBallob().getyBall());
    }

    public void shakeScreen() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), root);
        tt.setByX(20);
        tt.setCycleCount(8);
        tt.setAutoReverse(true);
        tt.setOnFinished(e -> root.setTranslateX(0));
        tt.play();
    }

    public void showGamePaused() {
        new GameScore().showGamePaused(this);
    }
    public void removeGamePaused() {
        new GameScore().removeGamePaused(this);
    }
    public void showGameOver() {
        new GameScore().showGameOver(this);
    }
    public void showWin() {
        new GameScore().showWin(this);
    }
    public void showMessage(String message) {new GameScore().showMessage(message, this);}
    public Button getLoad() {
        return load;
    }
    public Button getNewGame() {
        return newGame;
    }
    public void setLoadvisible(Boolean tf) {
        this.load.setVisible(tf);
    }
    public void setNewGamevisible(Boolean tf) {
        this.newGame.setVisible(tf);
    }
    public GameController getController() {
        return controller;
    }
}