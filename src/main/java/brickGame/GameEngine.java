package brickGame;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameEngine {

    private OnAction onAction;
    private Timeline updateTimeline;
    private Timeline physicsTimeline;
    private Timeline timeTimeline;
    private AnimationTimer timeTimer;
    private int fps;

    public GameEngine() {
        fps = 60;
    }

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void start() {
        initialize();
        startUpdateLoop();
        startPhysicsLoop();
        startTimeLoop();
    }

    public void stop() {
        if (updateTimeline != null) {
            updateTimeline.stop();
        }
        if (physicsTimeline != null) {
            physicsTimeline.stop();
        }
        if (timeTimer != null) {
            timeTimer.stop();
        }
    }

    private void initialize() {
        onAction.onInit();
    }

    private void startUpdateLoop() {
        Duration frameTime = Duration.millis(1000 / fps);
        updateTimeline = new Timeline(new KeyFrame(frameTime, e -> onAction.onUpdate()));
        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
    }

    private void startPhysicsLoop() {
        Duration frameTime = Duration.millis(1000 / fps);
        physicsTimeline = new Timeline(new KeyFrame(frameTime, e -> onAction.onPhysicsUpdate()));
        physicsTimeline.setCycleCount(Timeline.INDEFINITE);
        physicsTimeline.play();
    }

    private void startTimeLoop() {
        timeTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> onAction.onTime(System.currentTimeMillis())));
        timeTimeline.setCycleCount(Timeline.INDEFINITE);
        timeTimeline.play();
    }

    public interface OnAction {
        void onUpdate();
        void onInit();
        void onPhysicsUpdate();
        void onTime(long time);
    }
}
