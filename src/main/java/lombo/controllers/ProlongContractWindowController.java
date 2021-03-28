package lombo.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombo.DAO.DAO;
import lombo.model.*;
import lombo.utils.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProlongContractWindowController implements Initializable {

    private final SceneStageUtils utils = new SceneStageUtils();
    private final ContractFileUtils fileUtils = new ContractFileUtils();
    private final DAO dao = new DAO();

    private Contract oldContract = null;
    private Contract newContract = null;

    @FXML
    private TextField valueTextField;
    @FXML
    private DatePicker dateFromPicker;
    @FXML
    private TextField dateToField;
    @FXML
    private TextField interestValueField;
    @FXML
    private ComboBox<ContractType> typeComboBox;
    @FXML
    private ComboBox<ContractLength> lengthComboBox;
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private Button saveContractButton;
    @FXML
    private Button printContractButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ElementPropertiesUtils.fillContractLengthComboBox(FXCollections.observableArrayList(dao.getAllContractLengths()), lengthComboBox);

       ElementPropertiesUtils.setCustomerConverter(customerComboBox);
       ElementPropertiesUtils.setProductConverter(productComboBox);
       ElementPropertiesUtils.setContractTypeConverter(typeComboBox);

        customerComboBox.setDisable(true);
        productComboBox.setDisable(true);
        typeComboBox.setDisable(true);
        printContractButton.setDisable(true);

        dateToField.setDisable(true);
        dateToField.setStyle("-fx-opacity: 1;");
        interestValueField.setDisable(true);
        interestValueField.setStyle("-fx-opacity: 1;");

        ElementPropertiesUtils.addContractDatePickerListener(dateFromPicker, lengthComboBox, dateToField);

        ElementPropertiesUtils.addContractLengthComboBoxListener(dateFromPicker, lengthComboBox, dateToField,
                valueTextField, interestValueField);

        ElementPropertiesUtils.addContractValueFieldListener(valueTextField, interestValueField, lengthComboBox);

    }

    public void setOldContract(Contract oldContract) {

        this.oldContract = oldContract;
    }

    public void setOldContractValues() {

        customerComboBox.setValue(oldContract.getCustomerByCustomerId());
        productComboBox.setValue(oldContract.getProductByProductId());
        lengthComboBox.setValue(oldContract.getContractLengthByLengthId());
        typeComboBox.setValue(oldContract.getContractTypeByTypeId());
        dateFromPicker.setValue(oldContract.getDateTo());
        valueTextField.setText(FilterUtils.formatDouble(oldContract.getValue()));
    }

    @FXML
    private void onContractSaveButtonClicked() throws IOException {

        if(!FilterUtils.checkIfCorrectDataProvided(productComboBox, customerComboBox, typeComboBox, lengthComboBox,
                dateFromPicker, valueTextField, dao)){

            utils.getDefaultAlertBox().showAndWait();
        }
        else{

            ContractType contractType = typeComboBox.getValue();
            Product product = productComboBox.getValue();

            newContract = new Contract();

            newContract.setContractNo(ContractFileUtils.generateContractNo());

            newContract.setCustomerByCustomerId(customerComboBox.getValue());

            newContract.setContractTypeByTypeId(contractType);

            newContract.setDateFrom(dateFromPicker.getValue());

            newContract.setDateTo( DateUtils.calculateDateTo(lengthComboBox.getValue(), dateFromPicker.getValue()));

            newContract.setContractLengthByLengthId(lengthComboBox.getValue());

            newContract.setValue(Double.parseDouble(FilterUtils.changeCommaToDot(valueTextField.getText())));

            newContract.setProductByProductId(product);

            newContract.setContractStatusByStatusId(dao.findContractStatusByName("Aktywna"));

            newContract.setParentContractId(oldContract.getContractId());

            oldContract.setContractStatusByStatusId(dao.findContractStatusByName("Zakończona"));
            dao.updateContract(oldContract);

            dao.saveContract(newContract);

            product.setProductStatusByStatusId(dao.findProductStatusByName("Aktywna umowa"));
            dao.updateProduct(product);

            saveContractButton.setDisable(true);
            printContractButton.setDisable(false);
        }
    }

    @FXML
    private void onContractGenerateButtonClicked(MouseEvent mouseEvent) throws IOException {

        fileUtils.writeAndOpenFile(ContractFileUtils.BLANK_CONTRACT_FILE_PATH, newContract);

        SceneStageUtils.getSourceStage(mouseEvent).close();
    }
}
