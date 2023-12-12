package brickGame;

import brickGame.Controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;
/**
 * The main class for the brickGame application.
 * It extends the JavaFX {@link Application} class.
 * Responsible for launching the application and starting the game.
 */
public class Main extends Application {
    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * The overridden start method from the {@link Application} class.
     * Creates a new instance of {@link GameController} and starts the game.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        new GameController().start(primaryStage);
    }
}
