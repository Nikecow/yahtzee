package com.nikecow.yahtzee.gui;

import com.nikecow.yahtzee.game.Yahtzee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    static final Yahtzee y = new Yahtzee();
    static double prefHeight, prefWidth;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/gui.fxml"));
        root.getStylesheets().add("gui/stylesheet.css");
        primaryStage.setTitle(ApplicationInfo.getGlobalVariables("Title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setMinHeight(prefHeight + 39);
        primaryStage.setMinWidth(prefWidth + 16);
    }

}



