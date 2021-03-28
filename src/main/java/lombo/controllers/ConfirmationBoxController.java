package lombo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import lombo.utils.SceneStageUtils;

public class ConfirmationBoxController {

    @FXML
    private Label warningMessageLabel;

    private boolean answer = false;

    public void setWarningMessage(String warningMessage) {

        warningMessageLabel.setText(warningMessage);
    }

    public boolean getAnswer() {

        return answer;
    }

    @FXML
    private void onYesOptionClicked(MouseEvent mouseEvent) {

        answer = true;
        SceneStageUtils.getSourceStage(mouseEvent).close();
    }

    @FXML
    private void onNoOptionClicked(MouseEvent mouseEvent) {

        answer = false;
        SceneStageUtils.getSourceStage(mouseEvent).close();
    }
}
