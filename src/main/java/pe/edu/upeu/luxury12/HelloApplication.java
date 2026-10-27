
package pe.edu.upeu.luxury12;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/pe/edu/upeu/luxury12/view/view/portada-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("HOTEL LUXURY - Sistema de Gestión Hotelera");

        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
