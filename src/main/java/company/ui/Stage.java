package company.ui;

import company.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by Necros on 31.03.2015.
 */
@org.springframework.stereotype.Service
public class Stage extends Application {
    private Controller controller;
    private static final Logger logger = LoggerFactory.getLogger(Stage.class);
    public Stage() {
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        controller = (Controller) SpringApplicationContext.getBean("controller");
        FXMLLoader loader = new FXMLLoader();
        URL location = getClass().getResource("/company/sample.fxml");
        loader.setLocation(location);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Проверка реестров");
        primaryStage.show();
    }
    public void start(){
        launch();
    }
}
