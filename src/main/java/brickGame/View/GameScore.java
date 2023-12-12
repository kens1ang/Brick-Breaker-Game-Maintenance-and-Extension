package brickGame.View;

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
/**
 * The GameScore class manages the display of scores, messages, and game states in a JavaFX application.
 * It provides methods to show scores, messages, game over/win screens, and handles animations for labels.
 *
 * @author Your Name
 * @version 1.0
 */
public class GameScore {
    /**
     * Sets the position and size for a label.
     *
     * @param label The JavaFX Label to set position and size for.
     */
    private void setposnsizelabel(Label label) {
        label.setTranslateY(320);
        label.setTranslateX(200);
        label.setScaleX(1);
        label.setScaleY(1);
    }
    /**
     * Displays a score label with animation at the specified coordinates.
     *
     * @param x     The x-coordinate for the label.
     * @param y     The y-coordinate for the label.
     * @param score The score value to display.
     * @param view  The GameView associated with the game.
     */
    public void show(final double x, final double y, int score, final GameView view) {
        String sign = score >= 0 ? "+" : "";
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);
        Platform.runLater(() -> view.root.getChildren().add(label));
        animateLabel(label);
    }
    /**
     * Displays a message label with animation.
     *
     * @param message The message text to display.
     * @param view    The GameView associated with the game.
     */
    public void showMessage(String message, final GameView view) {
        final Label label = new Label(message);
        setposnsizelabel(label);
        Platform.runLater(() -> view.root.getChildren().add(label));
        animateLabel(label);
    }
    /**
     * Animates a label using scale and opacity changes.
     *
     * @param label The JavaFX Label to animate.
     */
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
    /**
     * Displays a "Game Over" message and restart button.
     *
     * @param view The GameView associated with the game.
     */
    public void showGameOver(final GameView view) {
        Platform.runLater(() -> {
            Label label = createLabel("Game Over :(");
            setposnsizelabel(label);
            Button restart = createButton(event -> view.getController().restartGame());
            view.root.getChildren().addAll(label, restart);
        });
    }
    /**
     * Displays a "You Win" message and restart button.
     *
     * @param view The GameView associated with the game.
     */
    public void showWin(final GameView view) {
        Platform.runLater(() -> {
            Label label = createLabel("You Win :)");
            setposnsizelabel(label);
            Button restart = createButton(event -> view.getController().restartGame());
            view.root.getChildren().addAll(label, restart);
        });
    }
    /**
     * Displays a "Game Paused" message.
     *
     * @param view The GameView associated with the game.
     */
    public void showGamePaused(final GameView view) {
        Platform.runLater(() -> {
            Label label = createLabel("Game Paused");
            setposnsizelabel(label);
            view.root.getChildren().add(label);
        });
    }
    /**
     * Removes the "Game Paused" message from the view.
     *
     * @param view The GameView associated with the game.
     */
    public void removeGamePaused(final GameView view) {
        Platform.runLater(() -> {
            ObservableList<Node> children = view.root.getChildren();
            children.removeIf(node -> node instanceof Label && ((Label) node).getText().equals("Game Paused"));
        });
    }
    /**
     * Creates a label with the specified text.
     *
     * @param text The text to be displayed on the label.
     * @return A new JavaFX Label with the specified text.
     */
    private Label createLabel(String text) {
        return new Label(text);
    }
    /**
     * Creates a restart button with the specified event handler.
     *
     * @param handler The event handler for the restart button.
     * @return A new JavaFX Button configured for restarting the game.
     */
    private Button createButton(EventHandler<ActionEvent> handler) {
        Button button = new Button("Restart");
        button.setTranslateX(220);
        button.setTranslateY(370);
        button.setOnAction(handler);
        return button;
    }
}
