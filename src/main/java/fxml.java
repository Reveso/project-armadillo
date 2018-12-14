import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class fxml {
//    @FXML
//    private RadioButton solo;
    @FXML
    private BorderPane mainPain;
    @FXML
    private Button AIvsAI;
    @FXML
    private Button AIvsP;
    @FXML
    private Button PvP;


    private FXMLLoader loader;
    //    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/test.fxml"));
    private BorderPane subPain;git

    public void AIvsAI(ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/AIvsAI.fxml"));
        subPain = loader.load();
        mainPain.setRight(subPain);
    }

    public void callPvP(ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/PvP.fxml"));
        subPain = loader.load();
        mainPain.setRight(subPain);
    }

    public void callAIvsP(ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/AIvsP.fxml"));
        subPain = loader.load();
        mainPain.setRight(subPain);
    }
}
