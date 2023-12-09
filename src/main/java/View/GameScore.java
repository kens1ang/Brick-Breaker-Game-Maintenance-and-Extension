package View;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameScore {

    private void setposnsizelabel(Label label) {
        label.setTranslateY(320);
        label.setTranslateX(200);
        label.setScaleX(1);
        label.setScaleY(1);
    }

    public void show(final double x, final double y, int score, final GameView view) {
        String sign = score >= 0 ? "+" : "";
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);
        Platform.runLater(() -> view.root.getChildren().add(label));
        animateLabel(label);
    }

    public void showMessage(String message, final GameView view) {
        final Label label = new Label(message);
        setposnsizelabel(label);
        Platform.runLater(() -> view.root.getChildren().add(label));
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

            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * ((double) totalAnimationDuration / animationSteps)), e -> {
                label.setScaleX(scale);
                label.setScaleY(scale);
                label.setOpacity(opacity);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(1);
        timeline.play();
    }

    public void showGameOver(final GameView view) {
        Platform.runLater(() -> {
            Label label = createLabel("Game Over :(");
            setposnsizelabel(label);
            Button restart = createButton(event -> view.getController().restartGame());
            view.root.getChildren().addAll(label, restart);
        });
    }

    public void showWin(final GameView view) {
        Platform.runLater(() -> {
            Label label = createLabel("You Win :)");
            setposnsizelabel(label);
            Button restart = createButton(event -> view.getController().restartGame());
            view.root.getChildren().addAll(label, restart);
        });
    }

    public void showGamePaused(final GameView view) {
        Platform.runLater(() -> {
            Label label = createLabel("Game Paused");
            setposnsizelabel(label);
            view.root.getChildren().add(label);
        });
    }

    public void removeGamePaused(final GameView view) {
        Platform.runLater(() -> {
            ObservableList<Node> children = view.root.getChildren();
            children.removeIf(node -> node instanceof Label && ((Label) node).getText().equals("Game Paused"));
        });
    }

    private Label createLabel(String text) {
        return new Label(text);
    }

    private Button createButton(EventHandler<ActionEvent> handler) {
        Button button = new Button("Restart");
        button.setTranslateX(220);
        button.setTranslateY(370);
        button.setOnAction(handler);
        return button;
    }
}
