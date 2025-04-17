package buffer.visualization;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
        // The main() method launches the app. i.e starts javafx 3d application
    }

    
    @Override
public void start(Stage stage) {
    try {
        CollisionViewer viewer = new CollisionViewer();
        Scene scene = new Scene(viewer.createContent(), 800, 600, true);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        //Makes objects look 3D (not flat).
        camera.setTranslateZ(-50);
        //moves camera back so we can see the scene(like zooming out)
        scene.setCamera(camera);
        stage.setScene(scene);
        //displaying everything
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
    }
}
}
