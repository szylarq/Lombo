package lombo.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombo.DAO.DAO;
import lombo.model.*;
import lombo.utils.ElementPropertiesUtils;
import lombo.utils.DateUtils;
import lombo.utils.FilterUtils;
import lombo.utils.SceneStageUtils;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomersController implements Initializable {

    private final SceneStageUtils utils = new SceneStageUtils();
    private final DAO dao = new DAO();

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField personalIdNoField;
    @FXML
    private Label todayDateLabel;
    @FXML
    private Label countLabel;
    @FXML
    private TableView<Customer> customersTableView;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> surnameColumn;
    @FXML
    private TableColumn<Customer, String> personalIdNoColumn;
    @FXML
    private TableColumn<Customer, String> issuedByColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> fatherNameColumn;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private Button editCustomerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        todayDateLabel.setText(DateUtils.getDayOfWeekString(LocalDate.now()));

        ObservableList<Customer> customers = FXCollections.observableArrayList(dao.getAllCustomers());
        fillTableView(customers);

        ElementPropertiesUtils.setTableViewSelectionListener(customersTableView, editCustomerButton, deleteCustomerButton);

        ElementPropertiesUtils.setCustomerNameFieldPromptText(nameField);
        ElementPropertiesUtils.setCustomerSurnameFieldPromptText(surnameField);
        ElementPropertiesUtils.setCustomerPersonalIdFieldPromptText(personalIdNoField);
    }

    private void fillTableView(ObservableList<Customer> customers) {



        customersTableView.setItems(customers);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        personalIdNoColumn.setCellValueFactory(element -> {

            String personalIdNo = element.getValue().getPersonalIdNo();

            return new SimpleStringProperty(personalIdNo.isEmpty() ? "Brak danych" : personalIdNo);
        });

        issuedByColumn.setCellValueFactory(element -> {

            String issuedBy = element.getValue().getIssuedBy();

            return new SimpleStringProperty(issuedBy.isEmpty() ? "Brak danych" : issuedBy);
        });

        addressColumn.setCellValueFactory(element -> {

            String address = element.getValue().getAddress();

            return new SimpleStringProperty(address.isEmpty() ? "Brak danych" : address);

        });

        fatherNameColumn.setCellValueFactory(element -> {

            String fatherName = element.getValue().getFatherName();

            return new SimpleStringProperty(fatherName.isEmpty() ? "Brak danych" : fatherName);

        });

        countLabel.setText("Łącznie: " + customers.size());
    }

    @FXML
    private void onFilterCustomersClicked() {

        fillTableView(FXCollections.observableArrayList(customersFilter(dao.getAllCustomers())));
    }

    private List<Customer> customersFilter(List<Customer> customers) {

        String name = nameField.getText();
        String surname = surnameField.getText();
        String personalIdNo = personalIdNoField.getText();

        Stream<Customer> customersStream = customers.stream();

        Map<Predicate<Customer>, Boolean> conditionPredicateMap = new HashMap<>();

        conditionPredicateMap.put(element -> element.getName().toLowerCase()
                .contains(name.toLowerCase()), name.length() == 0);

        conditionPredicateMap.put(element -> element.getSurname().toLowerCase()
                .contains(surname.toLowerCase()), surname.length() == 0);

        conditionPredicateMap.put(element -> element.getPersonalIdNo().toLowerCase()
                .contains(personalIdNo.toLowerCase()), personalIdNo.length() == 0);

        return FilterUtils.filterStreamByPredicates(customersStream, conditionPredicateMap).collect(Collectors.toList());
    }

    @FXML
    private void onCreateNewCustomerButtonClicked() throws IOException {

        Customer newCustomer = utils.getNewCustomerAndShowWindow();

        if (newCustomer != null) {

            dao.saveCustomer(newCustomer);

            onFilterCustomersClicked();
            customersTableView.refresh();
        }
    }

    @FXML
    private void onDeleteCustomerButtonClicked() throws IOException {

        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        Collection<Contract> relatedContracts = selectedCustomer.getContractsByCustomerId();

         if(relatedContracts == null || relatedContracts.isEmpty()) {

             Stage newConfirmationWindow = SceneStageUtils.getEmptyPopupWindow("Ostrzeżenie");
             FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.CONFIRMATION_BOX_FXML_FILE_PATH);
             newConfirmationWindow.setScene(new Scene(loader.load()));

             ConfirmationBoxController confirmationBoxController = loader.getController();

             confirmationBoxController.setWarningMessage("Dane klienta zostaną trwale usunięte z bazy danych. Czy chcesz kontynuować?");

             newConfirmationWindow.showAndWait();

             if(confirmationBoxController.getAnswer()){

                 dao.deleteCustomer(selectedCustomer);

                 onFilterCustomersClicked();
             }
         }
         else{

             utils.getCustomAlertBox("Nie można usunąć, ponieważ w bazie danych istnieją powiązane umowy!")
                     .showAndWait();
         }

    }

    @FXML
    private void onEditCustomerButtonClicked() throws IOException {

        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();

        Stage editCustomerWindow = SceneStageUtils.getEmptyPopupWindow("Edycja klienta");
        FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.CUSTOMER_WINDOW_FXML_FILE_PATH);
        editCustomerWindow.setScene(new Scene(loader.load()));

        CustomerWindowController customerWindowController = loader.getController();

        customerWindowController.setTitleLabelText("Edycja klienta");
        customerWindowController.setButtonText("Zapisz zmiany");

        customerWindowController.setNameFieldText(selectedCustomer.getName());
        customerWindowController.setSurnameFieldText(selectedCustomer.getSurname());
        customerWindowController.setPersonalIdNoFieldText(selectedCustomer.getPersonalIdNo());
        customerWindowController.setAddressFieldText(selectedCustomer.getAddress());
        customerWindowController.setFatherNameFieldText(selectedCustomer.getFatherName());
        customerWindowController.setIssuedByFieldText(selectedCustomer.getIssuedBy());

        editCustomerWindow.showAndWait();

        Customer customer = customerWindowController.getNewCustomer();

        if(customer != null){

            selectedCustomer.setName(customer.getName());
            selectedCustomer.setSurname(customer.getSurname());
            selectedCustomer.setPersonalIdNo(customer.getPersonalIdNo());
            selectedCustomer.setIssuedBy(customer.getIssuedBy());
            selectedCustomer.setAddress(customer.getAddress());
            selectedCustomer.setFatherName(customer.getFatherName());

            dao.updateCustomer(selectedCustomer);

            customersTableView.refresh();
        }
    }

    public void onResetNameFieldClicked() {

        nameField.setText("");
    }

    public void onResetSurnameClicked() {

        surnameField.setText("");
    }

    public void onResetPersonalIdNoFieldClicked() {

        personalIdNoField.setText("");
    }

    @FXML
    private void onHomeTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.HOME_FXML_FILE_PATH);
    }

    @FXML
    private void onContractsTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.CONTRACTS_FXML_FILE_PATH);
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
