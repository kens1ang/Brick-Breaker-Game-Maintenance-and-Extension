package Controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameEngine {

    private OnAction onAction;
    private Timeline updateTimeline;
    private Timeline physicsTimeline;
    private AnimationTimer timeTimer;
    private int fps;
    private boolean isPaused;

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

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public boolean isRunning() {
        return updateTimeline != null && physicsTimeline != null && timeTimer != null && !isPaused;
    }

    private void initialize() {
        onAction.onInit();
    }

    private void startUpdateLoop() {
        Duration frameTime = Duration.millis(1000.0 / fps);
        updateTimeline = new Timeline(new KeyFrame(frameTime, e -> {
            if (!isPaused) {
                onAction.onUpdate();
            }
        }));
        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
    }

    private void startPhysicsLoop() {
        Duration frameTime = Duration.millis(1000.0 / fps);
        physicsTimeline = new Timeline(new KeyFrame(frameTime, e -> {
            if (!isPaused) {
                onAction.onPhysicsUpdate();
            }
        }));
        physicsTimeline.setCycleCount(Timeline.INDEFINITE);
        physicsTimeline.play();
    }

    private void startTimeLoop() {
        timeTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    onAction.onTime(System.currentTimeMillis());
                }
            }
        };
        timeTimer.start();
    }

    public interface OnAction {
        void onUpdate();
        void onInit();
        void onPhysicsUpdate();
        void onTime(long time);
    }

}

