package brickGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Score {

    private static final int LABEL_X = 220;
    private static final int LABEL_Y = 340;
    private static final int GAME_OVER_LABEL_X = 200;
    private static final int GAME_OVER_LABEL_Y = 250;
    private static final int SCALE_FACTOR = 2;
//    private static final int ANIMATION_STEPS = 21;
//    private static final int ANIMATION_DELAY = 15;

    public void show(final double x, final double y, int score, final Main main) {
        String sign = score >= 0 ? "+" : "";
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);
        Platform.runLater(() -> main.root.getChildren().add(label));
        animateLabel(label);
    }

    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(LABEL_X);
        label.setTranslateY(LABEL_Y);
        Platform.runLater(() -> main.root.getChildren().add(label));
        animateLabel(label);
    }

    private void animateLabel(Label label) {
        int totalAnimationDuration = 1000;
        int animationSteps = 20;

        Timeline timeline = new Timeline();
        for (int i = 0; i <= animationSteps; i++) {
            double fraction = (double) i / animationSteps;
            double scale = 1 + 0.5 * fraction;
            double opacity = 1 - fraction;

            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * (totalAnimationDuration / animationSteps)), e -> {
                label.setScaleX(scale);
                label.setScaleY(scale);
                label.setOpacity(opacity);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(1);
        timeline.play();
    }

    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            Label label = createLabel("Game Over :(");
            Button restart = createButton(event -> main.restartGame());
            main.root.getChildren().addAll(label, restart);
        });
    }

    public void showWin(final Main main) {
        Platform.runLater(() -> {
            Label label = createLabel("You Win :)");
            main.root.getChildren().add(label);
        });
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setTranslateX(GAME_OVER_LABEL_X);
        label.setTranslateY(GAME_OVER_LABEL_Y);
        label.setScaleX(SCALE_FACTOR);
        label.setScaleY(SCALE_FACTOR);
        return label;
    }

    private Button createButton(EventHandler<ActionEvent> handler) {
        Button button = new Button("Restart");
        button.setTranslateX(LABEL_X);
        button.setTranslateY(370);
        button.setOnAction(handler);
        return button;
    }
}
