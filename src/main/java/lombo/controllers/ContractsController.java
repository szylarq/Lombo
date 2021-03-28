package lombo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombo.DAO.DAO;
import lombo.model.*;
import lombo.utils.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContractsController implements Initializable {

    private final SceneStageUtils utils = new SceneStageUtils();
    private final ContractFileUtils fileUtils = new ContractFileUtils();
    private final DAO dao = new DAO();

    @FXML
    private Button generateContractButton;
    @FXML
    private Button prolongContractButton;
    @FXML
    private Button loanRepaymentButton;
    @FXML
    private Button deleteContractButton;
    @FXML
    private TextField contractNoField;
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private ComboBox<ContractStatus> statusComboBox;
    @FXML
    private ComboBox<ContractType> typeComboBox;
    @FXML
    private DatePicker dateFromPicker;
    @FXML
    private DatePicker dateToPicker;
    @FXML
    private Label todayDateLabel;
    @FXML
    private Label countLabel;
    @FXML
    private TableView<Contract> contractsTableView;
    @FXML
    private TableColumn<Contract, String> contractNoColumn;
    @FXML
    private TableColumn<Contract, String> contractTypeColumn;
    @FXML
    private TableColumn<Contract, String> customerColumn;
    @FXML
    private TableColumn<Contract, String> dateFromColumn;
    @FXML
    private TableColumn<Contract, String> dateToColumn;
    @FXML
    private TableColumn<Contract, String> valueColumn;
    @FXML
    private TableColumn<Contract, String> rateColumn;
    @FXML
    private TableColumn<Contract, String> productColumn;
    @FXML
    private TableColumn<Contract, String> statusColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        todayDateLabel.setText(DateUtils.getDayOfWeekString(LocalDate.now()));

        ObservableList<Contract> contracts = FXCollections.observableArrayList(dao.getAllContracts());

        fillTableView(contracts);

        ElementPropertiesUtils.setStringDateComparator(dateFromColumn);
        ElementPropertiesUtils.setStringDateComparator(dateToColumn);

        generateContractButton.setDisable(true);
        loanRepaymentButton.setDisable(true);
        prolongContractButton.setDisable(true);
        deleteContractButton.setDisable(true);

        contractsTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {

                    Contract selectedContract = contractsTableView.getSelectionModel().getSelectedItem();

                    if(selectedContract == null){

                        generateContractButton.setDisable(true);
                        loanRepaymentButton.setDisable(true);
                        prolongContractButton.setDisable(true);
                        deleteContractButton.setDisable(true);
                    }
                    else if(selectedContract.getContractTypeByTypeId().equals(dao.findContractTypeByName("Sprzedaż"))
                            || selectedContract.getContractStatusByStatusId().equals(dao.findContractStatusByName("Zakończona"))){

                        generateContractButton.setDisable(false);
                        loanRepaymentButton.setDisable(true);
                        prolongContractButton.setDisable(true);
                        deleteContractButton.setDisable(false);

                    }
                    else{

                        deleteContractButton.setDisable(false);
                        generateContractButton.setDisable(false);
                        loanRepaymentButton.setDisable(false);
                        prolongContractButton.setDisable(false);
                    }
                });

        ElementPropertiesUtils.fillCustomersComboBox(FXCollections.observableArrayList(dao.getAllCustomers()), customerComboBox);
        ElementPropertiesUtils.fillProductsComboBox(FXCollections.observableArrayList(dao.getAllProducts()), productComboBox);
        ElementPropertiesUtils.fillContractTypeComboBox(FXCollections.observableArrayList(dao.getAllContractTypes()), typeComboBox);
        ElementPropertiesUtils.fillContractStatusComboBox(FXCollections.observableArrayList(dao.getAllContractStatuses()), statusComboBox);

        ElementPropertiesUtils.setContractNoFieldPromptText(contractNoField);
        ElementPropertiesUtils.setCustomerComboBoxPromptText(customerComboBox);
        ElementPropertiesUtils.setProductComboBoxPromptText(productComboBox);
        ElementPropertiesUtils.setDatePickerPromptText(dateFromPicker);
        ElementPropertiesUtils.setDatePickerPromptText(dateToPicker);
        ElementPropertiesUtils.setContractStatusComboBoxPromptText(statusComboBox);
        ElementPropertiesUtils.setContractTypeComboBoxPromptText(typeComboBox);
    }

    @FXML
    private void onCreateNewContractButtonClicked() throws IOException {

        utils.getNewPopupWindow(SceneStageUtils.CONTRACT_WINDOW_FXML_FILE_PATH, "Nowa umowa").showAndWait();

        onFilterContractsClicked();
    }
    @FXML
    private void onContractProlongButtonClicked() throws IOException {

        Stage prolongContractWindow = SceneStageUtils.getEmptyPopupWindow("Przedłużenie umowy");
        FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.PROLONG_CONTRACT_FILE_PATH);
        prolongContractWindow.setScene(new Scene(loader.load()));

        ProlongContractWindowController prolongContractWindowController = loader.getController();

        prolongContractWindowController.setOldContract(contractsTableView.getSelectionModel().getSelectedItem());
        prolongContractWindowController.setOldContractValues();

        prolongContractWindow.showAndWait();

        onFilterContractsClicked();
        contractsTableView.refresh();
    }
    @FXML
    private void onContractGenerateButtonClicked() throws IOException {

        fileUtils.writeAndOpenFile(
                ContractFileUtils.BLANK_CONTRACT_FILE_PATH, contractsTableView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void onDeleteContractButtonClicked() throws IOException {

        Contract selectedContract = contractsTableView.getSelectionModel().getSelectedItem();

        Stage newConfirmationWindow = SceneStageUtils.getEmptyPopupWindow("Ostrzeżenie");
        FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.CONFIRMATION_BOX_FXML_FILE_PATH);
        newConfirmationWindow.setScene(new Scene(loader.load()));

        ConfirmationBoxController confirmationBoxController = loader.getController();

        confirmationBoxController.setWarningMessage("Wybrana umowa zostanie trwale usunięta z bazy danych. Czy chcesz kontynuować?");

        newConfirmationWindow.showAndWait();

        if(confirmationBoxController.getAnswer()){

            if(selectedContract.getContractStatusByStatusId().equals(dao.findContractStatusByName("Aktywna"))) {

                Product relatedProduct = selectedContract.getProductByProductId();
                dao.updateProduct(relatedProduct);

                relatedProduct.setProductStatusByStatusId(dao.findProductStatusByName("Nieznany"));
            }

            dao.deleteContract(selectedContract);

            onFilterContractsClicked();
        }
    }

    @FXML
    private void onLoanRepaymentButtonClicked() throws IOException {

        Contract selectedContract = contractsTableView.getSelectionModel().getSelectedItem();

        FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.CONFIRMATION_BOX_FXML_FILE_PATH);
        Scene confirmationBoxScene = new Scene(loader.load());

        Stage newRepaymentConfirmationWindow = SceneStageUtils.getEmptyPopupWindow("Ostrzeżenie");
        newRepaymentConfirmationWindow.setScene(confirmationBoxScene);

        ConfirmationBoxController repaymentConfirmationBoxController = loader.getController();

        repaymentConfirmationBoxController.setWarningMessage("Status wybranej umowy zostanie zmieniony na Zakończona. Czy chcesz kontynuować?");

        newRepaymentConfirmationWindow.showAndWait();

        if(repaymentConfirmationBoxController.getAnswer()){

            selectedContract.setContractStatusByStatusId(dao.findContractStatusByName("Zakończona"));

            Product relatedProduct = selectedContract.getProductByProductId();

            relatedProduct.setProductStatusByStatusId(dao.findProductStatusByName("Zwrócono"));

            dao.updateProduct(relatedProduct);
            dao.updateContract(selectedContract);

            contractsTableView.refresh();


        }

        Stage newConfirmationPrintWindow = SceneStageUtils.getEmptyPopupWindow("Druk zwrotu pożyczki");
        newConfirmationPrintWindow.setScene(confirmationBoxScene);

        ConfirmationBoxController confirmationPrintBoxController = loader.getController();

        confirmationPrintBoxController.setWarningMessage("Czy chcesz wydrukować druk zwrotu pożyczki z sugerowanymi danymi?");

        newConfirmationPrintWindow.showAndWait();

        if(repaymentConfirmationBoxController.getAnswer()){

            fileUtils.writeAndOpenFile(ContractFileUtils.BLANK_REPAYMENT_CONFIRMATION_FILE_PATH, selectedContract);
        }
    }

    @FXML
    public void onFilterContractsClicked() {

        fillTableView(FXCollections.observableArrayList(contractsFilter(dao.getAllContracts())));
    }
    private List<Contract> contractsFilter(List<Contract> contracts) {

        String contractNo = contractNoField.getText();
        Customer customer = customerComboBox.getValue();
        Product product = productComboBox.getValue();
        ContractStatus contractStatus = statusComboBox.getValue();
        ContractType contractType = typeComboBox.getValue();
        LocalDate dateFrom = dateFromPicker.getValue();
        LocalDate dateTo = dateToPicker.getValue();

        Stream<Contract> contractsStream = contracts.stream();

        Map<Predicate<Contract>, Boolean> conditionPredicateMap = new HashMap<>();

        conditionPredicateMap.put(element -> element.getContractNo().toLowerCase()
                .contains(contractNo.toLowerCase()), contractNo.length() == 0);

        conditionPredicateMap.put(element -> element.getCustomerByCustomerId().equals(customer), customer == null);

        conditionPredicateMap.put(element -> element.getProductByProductId().equals(product), product == null);

        conditionPredicateMap.put(element -> element.getContractStatusByStatusId().equals(contractStatus), contractStatus == null);

        conditionPredicateMap.put(element -> element.getContractTypeByTypeId().equals(contractType), contractType == null);

        conditionPredicateMap.put(element -> DateUtils.isDateBetweenTwoDates(dateFrom, dateTo, element.getDateFrom()),
                dateFrom == null && dateTo == null);

        return FilterUtils.filterStreamByPredicates(contractsStream, conditionPredicateMap).collect(Collectors.toList());
    }

    private void fillTableView(ObservableList<Contract> contracts){

        ElementPropertiesUtils.fillContractTableView(contracts, countLabel, contractsTableView, contractNoColumn,
                contractTypeColumn, customerColumn, dateFromColumn, dateToColumn, valueColumn, rateColumn, productColumn,
                statusColumn, dao);
    }

    @FXML
    private void onResetContractNoFieldClicked() {

        contractNoField.setText("");
    }
    @FXML
    private void onResetCustomerComboBoxClicked() {

        customerComboBox.setValue(null);
        customerComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean isEmpty) {
                super.updateItem(customer, isEmpty);

                if (isEmpty || customer == null) {
                    setText("Wybierz klienta");
                }
                else {
                    setText(customer.getName() + " " + customer.getSurname());
                }
            }
        });
    }
    @FXML
    private void onResetProductComboBoxClicked() {

        productComboBox.setValue(null);
        productComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Product product, boolean isEmpty) {
                super.updateItem(product, isEmpty);

                if (isEmpty || product == null) {
                    setText("Wybierz produkt");
                }
                else {
                    setText(product.getName());
                }
            }
        });
    }
    @FXML
    private void onResetDateFromPickerClicked() {

        dateFromPicker.setValue(null);
        dateFromPicker.getEditor().clear();
        ElementPropertiesUtils.setDatePickerPromptText(dateFromPicker);
    }
    @FXML
    private void onResetDateToPickerClicked() {

        dateToPicker.setValue(null);
        dateToPicker.getEditor().clear();
        ElementPropertiesUtils.setDatePickerPromptText(dateToPicker);
    }
    @FXML
    private void onResetStatusComboBoxClicked() {

        statusComboBox.setValue(null);
        statusComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ContractStatus status, boolean isEmpty) {
                super.updateItem(status, isEmpty);

                if (isEmpty || status == null) {
                    setText("Wybierz status umowy");
                }
                else {
                    setText(status.getStatus());
                }
            }
        });
    }
    @FXML
    private void onResetTypeComboBoxClicked() {

        typeComboBox.setValue(null);
        typeComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ContractType type, boolean isEmpty) {
                super.updateItem(type, isEmpty);

                if (isEmpty || type == null) {
                    setText("Wybierz typ umowy");
                }
                else {
                    setText(type.getType());
                }
            }
        });
    }

    @FXML
    private void onHomeTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.HOME_FXML_FILE_PATH);
    }
    @FXML
    private void onCustomersTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.CUSTOMERS_FXML_FILE_PATH);
    }
    @FXML
    private void onProductsTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.PRODUCTS_FXML_FILE_PATH);
    }
    @FXML
    private void onCategoriesTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.CATEGORIES_FXML_FILE_PATH);
    }
}
