package lombo.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombo.DAO.DAO;
import lombo.model.Product;
import lombo.model.ProductCategory;
import lombo.utils.ElementPropertiesUtils;
import lombo.utils.FilterUtils;
import lombo.utils.SceneStageUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductWindowController implements Initializable {

    private final DAO dao = new DAO();
    private final SceneStageUtils utils = new SceneStageUtils();

    private Product newProduct = null;

    @FXML
    private TextField nameField;
    @FXML
    private TextField valueField;
    @FXML
    private ComboBox<ProductCategory> categoryComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Label titleLabel;

    public Product getNewProduct() {

        return newProduct;
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

    public void setValueField(String value) {

        valueField.setText(value);
    }

    public void setCategoryComboBoxValue(ProductCategory category) {

        categoryComboBox.setValue(category);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ElementPropertiesUtils.fillCategoryComboBox(FXCollections.observableArrayList(dao.getAllProductCategories()), categoryComboBox);

        ElementPropertiesUtils.setProductNameFieldPromptText(nameField);
        ElementPropertiesUtils.setValueFieldPromptText(valueField);
        ElementPropertiesUtils.setCategoryComboBoxPromptText(categoryComboBox);
    }


    @FXML
    private void onCreateNewCategoryClicked() throws IOException {

        categoryComboBox.setValue(null);
        categoryComboBox.setValue(utils.getNewCategoryAndShowWindow());
    }

    @FXML
    private void onSaveNewProductClicked(MouseEvent actionEvent) throws IOException {

        String productName = nameField.getText();
        String productValue = valueField.getText();
        ProductCategory category = categoryComboBox.getValue();

        if(productName.length() != 0 && FilterUtils.isStringValueDouble(productValue)){

            newProduct = new Product();
            newProduct.setName(productName);
            newProduct.setValue(Double.parseDouble(FilterUtils.changeCommaToDot(productValue)));
            newProduct.setProductCategoryByCategoryId(category);
            newProduct.setProductStatusByStatusId(dao.findProductStatusByName("Nieznany"));

            SceneStageUtils.getSourceStage(actionEvent).close();
        }
        else{

            utils.getDefaultAlertBox().showAndWait();
        }
    }
}
