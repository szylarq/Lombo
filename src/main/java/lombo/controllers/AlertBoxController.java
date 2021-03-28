package lombo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import lombo.utils.SceneStageUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertBoxController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        errorMessageLabel.setText("Wprowadź poprawne dane!");
    }

    @FXML
    private Label errorMessageLabel;

    public void setErrorMessage(String errorMessage) {

        errorMessageLabel.setText(errorMessage);
    }

    @FXML
    private void onOKButtonClicked(MouseEvent actionEvent) {

        SceneStageUtils.getSourceStage(actionEvent).close();
    }
}
