package lombo.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import lombo.DAO.DAO;
import lombo.model.*;
import lombo.utils.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ContractWindowController implements Initializable {

    private final SceneStageUtils utils = new SceneStageUtils();
    private final ContractFileUtils fileUtils = new ContractFileUtils();
    private final DAO dao = new DAO();

    private Contract newContract = null;

    @FXML
    private Label valueLabel;
    @FXML
    private Button saveContractButton;
    @FXML
    private Button printContractButton;
    @FXML
    private TextField valueTextField;
    @FXML
    private DatePicker dateFromPicker;
    @FXML
    private TextField dateToField;
    @FXML
    private TextField interestValueField;
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private ComboBox<ContractLength> lengthComboBox;
    @FXML
    private ComboBox<ContractType> typeComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ElementPropertiesUtils.setValueFieldPromptText(valueTextField);
        ElementPropertiesUtils.setDatePickerPromptText(dateFromPicker);
        ElementPropertiesUtils.setCustomerComboBoxPromptText(customerComboBox);
        ElementPropertiesUtils.setProductComboBoxPromptText(productComboBox);
        ElementPropertiesUtils.setContractLengthComboBoxPromptText(lengthComboBox);
        ElementPropertiesUtils.setContractTypeComboBoxPromptText(typeComboBox);

        dateFromPicker.setValue(LocalDate.now());

        dateToField.setDisable(true);
        dateToField.setStyle("-fx-opacity: 1;");
        interestValueField.setDisable(true);
        interestValueField.setStyle("-fx-opacity: 1;");


        ElementPropertiesUtils.fillProductsComboBox(FXCollections.observableArrayList(dao.getAllProducts()), productComboBox);
        ElementPropertiesUtils.fillContractTypeComboBox(FXCollections.observableArrayList(dao.getAllContractTypes()), typeComboBox);
        ElementPropertiesUtils.fillCustomersComboBox(FXCollections.observableArrayList(dao.getAllCustomers()), customerComboBox);
        ElementPropertiesUtils.fillContractLengthComboBox(FXCollections.observableArrayList(dao.getAllContractLengths()), lengthComboBox);

        printContractButton.setDisable(true);

        ElementPropertiesUtils.addContractDatePickerListener(dateFromPicker, lengthComboBox, dateToField);

        ElementPropertiesUtils.addContractLengthComboBoxListener(dateFromPicker, lengthComboBox, dateToField,
                valueTextField, interestValueField);

        ElementPropertiesUtils.addContractTypeComboBoxListener(typeComboBox, valueTextField, interestValueField,
                dateFromPicker, lengthComboBox, dateToField, valueLabel, dao);

        ElementPropertiesUtils.addContractValueFieldListener(valueTextField, interestValueField, lengthComboBox);
    }

    @FXML
    private void onCreateNewCustomerClicked() throws IOException {

        customerComboBox.setValue(null);
        customerComboBox.setValue(utils.getNewCustomerAndShowWindow());
    }
    @FXML
    private void onCreateNewProductClicked() throws IOException {

        productComboBox.setValue(null);
        productComboBox.setValue(utils.getNewProductAndShowWindow());
    }

    @FXML
    private void onContractSaveButtonClicked() throws IOException {

        if(!FilterUtils.checkIfCorrectDataProvided(productComboBox, customerComboBox, typeComboBox, lengthComboBox,
                dateFromPicker, valueTextField, dao)){

            utils.getDefaultAlertBox().showAndWait();
        }
        else{

            ContractType contractType = typeComboBox.getValue();
            Customer customer = customerComboBox.getValue();
            Product product = productComboBox.getValue();
            boolean isSellContract = contractType.equals(dao.findContractTypeByName("Sprzedaż"));

            newContract = new Contract();

            newContract.setContractNo(ContractFileUtils.generateContractNo());

            newContract.setCustomerByCustomerId(customer);

            newContract.setContractTypeByTypeId(contractType);

            newContract.setDateFrom(dateFromPicker.getValue());

            newContract.setDateTo(isSellContract ? null :
                    DateUtils.calculateDateTo(lengthComboBox.getValue(), dateFromPicker.getValue()));

            newContract.setContractLengthByLengthId(lengthComboBox.getValue());

            newContract.setValue(Double.parseDouble(FilterUtils.changeCommaToDot(valueTextField.getText())));

            newContract.setProductByProductId(product);

            newContract.setContractStatusByStatusId(dao.findContractStatusByName(isSellContract ?
                    "Zakończona" : "Aktywna"));

            newContract.setParentContractId(null);

            ProductCategory productCategory = product.getProductCategoryByCategoryId();

            if(productCategory != null && productCategory.getCategoryId() == null){

                dao.saveProductCategory(productCategory);
            }

            product.setProductStatusByStatusId(dao.findProductStatusByName(isSellContract ?
                    "Sprzedano" : "Aktywna umowa"));

            if(product.getProductId() == null){

                dao.saveProduct(product);
            }
            else{

                dao.updateProduct(product);
            }

            if(customer.getCustomerId() == null){

                dao.saveCustomer(customer);
            }

            dao.saveContract(newContract);

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
