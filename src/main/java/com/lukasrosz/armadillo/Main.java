package com.lukasrosz.armadillo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

//    public static void main(String[] args) {
//        //TODO Get user input on what game does he want
//        //TODO Create came by making GameMaker class instance, that will return the Scoreboard class
////        System.out.println(System.getProperty("user.dir"));
////        val gameMaker = new GameMaker();
////        gameMaker.newBattleGame(new File(System.getProperty("user.dir")), 16);
//    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(getClass());
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
