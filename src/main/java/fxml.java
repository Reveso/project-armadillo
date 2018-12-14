import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class fxml {
    @FXML
    private RadioButton solo;
    @FXML
    private BorderPane mainPain;

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/borderForfxml.fxml"));
    private BorderPane subPain;

    public void method(ActionEvent actionEvent) throws IOException {
            subPain = loader.load();
//            subPain.
        Stage stage = (Stage) mainPain.getScene().getWindow();
//        stage.close();


        mainPain.setRight(subPain);
    }

}
