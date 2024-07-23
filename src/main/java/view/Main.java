package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

import Model.Model;
import view_model.ViewModel;

// FlightGear connection: --telnet=socket,in,10,127.0.0.1,5402,tcp
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/mvvm1flight/view/Window.fxml"));
        Pane root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Model m = new Model("properties.txt");
        WindowController wc = fxmlLoader.getController();
        ViewModel vm = new ViewModel(m);
        wc.init(vm);

        // Initialize the sliders and model values
        wc.paint();

        // Set the scene and show the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("FlightGear Controller");
        primaryStage.show();

        // Add key event handling
        scene.setOnKeyPressed(event -> wc.keyPress(event));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
