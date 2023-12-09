module brickGame {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.logging;
    requires javafx.media;

    opens brickGame to javafx.fxml;
    exports brickGame;
    exports Model;
    opens Model to javafx.fxml;
    exports View;
    opens View to javafx.fxml;
    exports Controller;
    opens Controller to javafx.fxml;
    exports GameElements;
    opens GameElements to javafx.fxml;
}