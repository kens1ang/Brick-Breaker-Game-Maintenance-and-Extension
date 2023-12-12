package brickGame.Controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
/**
 * The GameEngine class manages the game loop and provides hooks for various game-related actions.
 * It includes separate timelines for updating, physics calculations, and time tracking.
 */
public class GameEngine {

    private OnAction onAction;
    private Timeline updateTimeline;
    private Timeline physicsTimeline;
    private AnimationTimer timeTimer;
    private int fps;
    private boolean isPaused;
    /**
     * Creates a new GameEngine with a default frames-per-second value of 60.
     */
    public GameEngine() {
        fps = 60;
    }
    /**
     * Sets the OnAction interface to receive callbacks for game-related actions.
     *
     * @param onAction The implementation of the OnAction interface.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }
    /**
     * Sets the frames per second (fps) for the game loop.
     *
     * @param fps The desired frames per second.
     */
    public void setFps(int fps) {
        this.fps = fps;
    }
    /**
     * Starts the game engine, initializing the game and beginning the update, physics, and time loops.
     */
    public void start() {
        initialize();
        startUpdateLoop();
        startPhysicsLoop();
        startTimeLoop();
    }
    /**
     * Stops the game engine, halting the update, physics, and time loops.
     */
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
    /**
     * Pauses the game engine, halting the update, physics, and time loops.
     */
    public void pause() {
        isPaused = true;
    }
    /**
     * Resumes the game engine after being paused, restarting the update, physics, and time loops.
     */
    public void resume() {
        isPaused = false;
    }
    /**
     * Checks if the game engine is currently running.
     *
     * @return {@code true} if the game engine is running; {@code false} otherwise.
     */
    public boolean isRunning() {
        return updateTimeline != null && physicsTimeline != null && timeTimer != null && !isPaused;
    }
    /**
     * Initializes the game engine by invoking the {@code onInit()} method.
     * This method is called during the start of the game.
     */
    private void initialize() {
        onAction.onInit();
    }
    /**
     * Starts the update loop, which invokes {@code onUpdate()} at the specified frames per second.
     * This method is part of the game engine's internal functioning.
     */
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
    /**
     * Starts the physics loop, which invokes {@code onPhysicsUpdate()} at the specified frames per second.
     * This method is part of the game engine's internal functioning.
     */
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
    /**
     * Starts the time loop, which tracks the passage of time and invokes {@code onTime()} on each frame.
     * This method is part of the game engine's internal functioning.
     */
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
    /**
     * The OnAction interface defines callback methods for various game-related actions.
     */
    public interface OnAction {
        /**
         * Called on each frame to handle general game updates.
         */
        void onUpdate();
        /**
         * Called during game initialization to perform setup actions.
         */
        void onInit();
        /**
         * Called on each frame to handle physics-related updates.
         */
        void onPhysicsUpdate();
        /**
         * Called on each frame to track the passage of time.
         *
         * @param time The current time in milliseconds.
         */
        void onTime(long time);
    }

}

