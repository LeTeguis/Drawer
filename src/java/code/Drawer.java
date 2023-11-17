import controller.DrawerMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Drawer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/drawer_main.fxml"));
        BorderPane root = loader.load();

        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Drawer");
        primaryStage.show();
    }
}
