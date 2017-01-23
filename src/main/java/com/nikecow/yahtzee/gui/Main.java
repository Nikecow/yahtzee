package com.nikecow.yahtzee.gui;

import com.nikecow.yahtzee.game.Yahtzee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    static final Yahtzee y = new Yahtzee();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/gui.fxml"));
        root.getStylesheets().add("gui/stylesheet.css");
        // Set the title of the application to the title defined in the MANIFEST.MF
        primaryStage.setTitle(ApplicationInfo.getGlobalVariables("Title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        // Get the current height after the stage is shown and set that as a minimum
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());
    }

}



