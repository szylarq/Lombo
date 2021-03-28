package lombo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombo.DAO.DAO;
import lombo.model.ProductCategory;
import lombo.utils.ElementPropertiesUtils;
import lombo.utils.SceneStageUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductCategoryWindowController implements Initializable {

    private final DAO dao = new DAO();
    private final SceneStageUtils utils = new SceneStageUtils();

    private ProductCategory newCategory = null;

    @FXML
    private TextField nameField;
    @FXML
    private Label titleLabel;
    @FXML
    private Button saveButton;

    public ProductCategory getNewCategory() {

        return newCategory;
    }

    public void setTitleLabelText(String labelText) {

        titleLabel.setText(labelText);
    }

    public void setButtonText(String buttonText){

        saveButton.setText(buttonText);
    }

    public void setNameFieldText(String nameText){

        nameField.setText(nameText);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ElementPropertiesUtils.setCategoryNameFieldPromptText(nameField);
    }
    @FXML
    private void onSaveNewCategoryClicked(MouseEvent actionEvent) throws IOException {

        String categoryName = nameField.getText();

        if(categoryName.length() != 0 && dao.findProductCategoryByName(categoryName) == null){

            newCategory = new ProductCategory();
            newCategory.setName(categoryName);

            SceneStageUtils.getSourceStage(actionEvent).close();
        }
        else {

            Stage newAlertWindow = SceneStageUtils.getEmptyPopupWindow("Błąd");
            FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.ALERT_BOX_FXML_FILE_PATH);
            newAlertWindow.setScene(new Scene(loader.load()));

            AlertBoxController alertBoxController = loader.getController();

            if(categoryName.length() == 0){

                alertBoxController.setErrorMessage("Wprowadź nazwę nowej kategorii!");
            }
            else{

                alertBoxController.setErrorMessage("Istnieje już kategoria o takiej nazwie!");
            }

            newAlertWindow.showAndWait();
        }
    }
}
