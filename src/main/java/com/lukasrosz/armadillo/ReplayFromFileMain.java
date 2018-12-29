package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.controller.FightSceneController;
import com.lukasrosz.armadillo.controller.ReplayController;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import com.lukasrosz.armadillo.replay.GameReplay;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.val;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ReplayFromFileMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        File file = new File("referee_files/replays/12_29_2018_04_57_06_JP 100 + VAT_test-555555.rep");
        JAXBContext jaxbContext = JAXBContext.newInstance(GameReplay.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        GameReplay gameReplay = (GameReplay) jaxbUnmarshaller.unmarshal(file);
        System.out.println(gameReplay);

        primaryStage.setTitle(gameReplay.getGameResult().getWinner().getAlias() + " vs "
                            + gameReplay.getGameResult().getLoser().getAlias());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/replay-scene.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        ReplayController replayController = fxmlLoader.getController();

        replayController.setup(gameReplay, primaryStage.getScene(), "reee",
                Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.setScene(new Scene(fightStageRoot));
        primaryStage.setOnCloseRequest(event -> onExitClicked());
        primaryStage.show();
    }

    private void onExitClicked() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
//_JP 100% +