package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the expense tracker.
 * Launches the JavaFX application.
 */
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/edu/westga/comp2320/project3part3cedricjones/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Project 3 Finance");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main.
     * @param args cl arguments
     */
    public static void main(String[] args) {
        launch();
    }
}