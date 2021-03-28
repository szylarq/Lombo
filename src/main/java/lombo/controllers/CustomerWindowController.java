package lombo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombo.model.Customer;
import lombo.utils.ElementPropertiesUtils;
import lombo.utils.SceneStageUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerWindowController implements Initializable {

    private final SceneStageUtils utils = new SceneStageUtils();

    private Customer newCustomer = null;

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField personalIdNoField;
    @FXML
    private TextField issuedByField;
    @FXML
    private TextField fatherNameField;
    @FXML
    private TextField addressField;
    @FXML
    private Button saveButton;
    @FXML
    private Label titleLabel;

    public void setNameFieldText(String nameText) {

        nameField.setText(nameText);
    }

    public void setSurnameFieldText(String surnameText) {

        surnameField.setText(surnameText);
    }

    public void setPersonalIdNoFieldText(String personalIdNoText) {

        personalIdNoField.setText(personalIdNoText);
    }

    public void setIssuedByFieldText(String issuedByText) {

        issuedByField.setText(issuedByText);
    }

    public void setFatherNameFieldText(String fatherNameText) {

        fatherNameField.setText(fatherNameText);
    }

    public void setAddressFieldText(String addressText) {

        addressField.setText(addressText);
    }

    public void setButtonText(String text) {

        saveButton.setText(text);
    }

    public void setTitleLabelText(String labelText) {

        titleLabel.setText(labelText);
    }

    public Customer getNewCustomer() {

        return newCustomer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ElementPropertiesUtils.setCustomerNameFieldPromptText(nameField);
        ElementPropertiesUtils.setCustomerSurnameFieldPromptText(surnameField);
        ElementPropertiesUtils.setCustomerPersonalIdFieldPromptText(personalIdNoField);
        ElementPropertiesUtils.setIssuedByFieldPromptText(issuedByField);
        ElementPropertiesUtils.setCustomerFatherNameFieldPromptText(fatherNameField);
        ElementPropertiesUtils.setCustomerAddressFieldPromptText(addressField);
    }

    public void onSaveNewCustomerClicked(MouseEvent actionEvent) throws IOException {

        String name = nameField.getText();
        String surname = surnameField.getText();

        if(!name.isEmpty() && !surname.isEmpty()){

            newCustomer = new Customer();
            newCustomer.setName(name);
            newCustomer.setSurname(surname);
            newCustomer.setPersonalIdNo(personalIdNoField.getText());
            newCustomer.setIssuedBy(issuedByField.getText());
            newCustomer.setAddress(addressField.getText());
            newCustomer.setFatherName(fatherNameField.getText());

            SceneStageUtils.getSourceStage(actionEvent).close();
        }
        else{

            utils.getDefaultAlertBox().showAndWait();
        }
    }
}
