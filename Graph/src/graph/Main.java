package graph;

import elements.PannableCanvas;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static App app;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.vsync", "false");
        System.setProperty("prism.text", "t2k");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
//        Font.loadFont(getClass().getResourceAsStream("/education/font.ttf"), 16);
        Scene scene = new Scene(root, 1200, 800);
        app = loader.getController();
        app.set(scene, primaryStage);
        primaryStage.setTitle("Test");
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
