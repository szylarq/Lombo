package lombo.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import lombo.DAO.DAO;
import lombo.model.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class ElementPropertiesUtils {

    public static void fillCustomersComboBox(ObservableList<Customer> customers, ComboBox<Customer> customerComboBox){

        customerComboBox.setItems(customers);
        setCustomerConverter(customerComboBox);
    }

    public static void setCustomerConverter(ComboBox<Customer> customerComboBox){

        customerComboBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(Customer customer) {

                return customer == null ? null : customer.getSurname() + " " + customer.getName();
            }
            @Override
            public Customer fromString(String s) {
                return null;
            }
        });
    }

    public static void fillProductsComboBox(ObservableList<Product> products, ComboBox<Product> productComboBox){

        productComboBox.setItems(products);
        setProductConverter(productComboBox);
    }

    public static void setProductConverter(ComboBox<Product> productComboBox){

        productComboBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(Product product) {

                return product == null ? null : product.getName() + " | " + FilterUtils.formatDouble(product.getValue());
            }
            @Override
            public Product fromString(String s) {
                return null;
            }
        });
    }

    public static void fillContractTypeComboBox(ObservableList<ContractType> contractTypes,
                                                ComboBox<ContractType> typeComboBox){
        typeComboBox.setItems(contractTypes);
        setContractTypeConverter(typeComboBox);

    }

    public static void setContractTypeConverter(ComboBox<ContractType> typeComboBox){

        typeComboBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(ContractType contractType) {
                return contractType == null ? null : contractType.getType();
            }
            @Override
            public ContractType fromString(String s) {
                return null;
            }
        });
    }

    public static void fillContractStatusComboBox(ObservableList<ContractStatus> contractStatuses,
                                                 ComboBox<ContractStatus> statusComboBox){

        statusComboBox.setItems(contractStatuses);

        statusComboBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(ContractStatus contractStatus) {

                return contractStatus == null ? null : contractStatus.getStatus();
            }
            @Override
            public ContractStatus fromString(String s) {
                return null;
            }
        });
    }

    public static void fillCategoryComboBox(ObservableList<ProductCategory> productCategories,
                                            ComboBox<ProductCategory> categoryComboBox) {

        categoryComboBox.setItems(productCategories);

        categoryComboBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(ProductCategory category) {

                return category == null ? null : category.getName();
            }
            @Override
            public ProductCategory fromString(String s) {
                return null;
            }
        });
    }

    public static void fillContractLengthComboBox(ObservableList<ContractLength> contractLengths,
                                            ComboBox<ContractLength> lengthComboBox) {

        lengthComboBox.setItems(contractLengths);

        lengthComboBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(ContractLength length) {

                return length == null ? null : length.getLength() +" " + length.getStorageRate() + "% + "
                        + length.getLoanRate() + "%";
            }
            @Override
            public ContractLength fromString(String s) {
                return null;
            }
        });
    }

    public static void fillProductStatusComboBox(ObservableList<ProductStatus> productStatuses,
                                                 ComboBox<ProductStatus> statusComboBox){

        statusComboBox.setItems(productStatuses);

        statusComboBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(ProductStatus status) {

                return status == null ? null : status.getStatus();
            }
            @Override
            public ProductStatus fromString(String s) {
                return null;
            }
        });
    }

    public static void setStringDateComparator(TableColumn<Contract, String> dateColumn){

        dateColumn.setComparator((o1, o2) -> {

            Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");

            if (!pattern.matcher(o1).matches() && !pattern.matcher(o2).matches()) {

                return 0;
            }
            else if (!pattern.matcher(o1).matches()) {

                return -1;
            }
            else if (!pattern.matcher(o2).matches()) {

                return 1;
            }
            else {
                return LocalDate.parse(DateUtils.convertPolishDateFormatToLocalDateFormat(o1))
                        .compareTo(LocalDate.parse(DateUtils.convertPolishDateFormatToLocalDateFormat(o2)));
            }
        });
    }

    public static void setDatePickerPromptText(DatePicker datePicker){

        datePicker.setPromptText("Wybierz datę");
    }

    public static void setCategoryComboBoxPromptText(ComboBox<ProductCategory> categoryComboBox){

        categoryComboBox.setPromptText("Wybierz kategorię");
    }

    public static void setProductStatusComboBoxPromptText(ComboBox<ProductStatus> statusComboBox){

        statusComboBox.setPromptText("Wybierz status produktu");
    }

    public static void setCustomerComboBoxPromptText(ComboBox<Customer> customerComboBox){

        customerComboBox.setPromptText("Wybierz klienta");
    }

    public static void setProductComboBoxPromptText(ComboBox<Product> productComboBox){

        productComboBox.setPromptText("Wybierz produkt");
    }

    public static void setContractStatusComboBoxPromptText(ComboBox<ContractStatus> statusComboBox){

        statusComboBox.setPromptText("Wybierz status umowy");
    }

    public static void setContractTypeComboBoxPromptText(ComboBox<ContractType> typeComboBox){

        typeComboBox.setPromptText("Wybierz typ umowy");
    }

    public static void setContractNoFieldPromptText(TextField contractNoField){

        contractNoField.setPromptText("Wpisz numer umowy");
    }

    public static void setProductNameFieldPromptText(TextField productNameField){

        productNameField.setPromptText("Wpisz nazwę produktu");
    }

    public static void setValueFieldPromptText(TextField valueField){

        valueField.setPromptText("Wpisz wartość");
    }

    public static void setCustomerNameFieldPromptText(TextField customerNameField){

        customerNameField.setPromptText("Wpisz imię");
    }

    public static void setCustomerSurnameFieldPromptText(TextField customerSurnameField){

        customerSurnameField.setPromptText("Wpisz nazwisko");
    }

    public static void setCustomerAddressFieldPromptText(TextField customerAddressField){

        customerAddressField.setPromptText("Wpisz PESEL klienta");
    }

    public static void setIssuedByFieldPromptText(TextField issuedByField){

        issuedByField.setPromptText("Wpisz organ wydający dowód osobisty");
    }

    public static void setCustomerFatherNameFieldPromptText(TextField customerFatherNameField){

        customerFatherNameField.setPromptText("Wpisz imię ojca");
    }

    public static void setCustomerPersonalIdFieldPromptText(TextField customerPersonalIdField){

        customerPersonalIdField.setPromptText("Wpisz numer dowodu osobistego");
    }

    public static void setCategoryNameFieldPromptText(TextField categoryNameField){

        categoryNameField.setPromptText("Wpisz nazwę nowej kategorii");
    }

    public static void setContractLengthComboBoxPromptText(ComboBox<ContractLength> lengthComboBox){

        lengthComboBox.setPromptText("Wybierz długość umowy");
    }


    public static void setInterestValueField(TextField valueTextField, TextField interestValueField, ComboBox<ContractLength> lengthComboBox){

        String value = valueTextField.getText();
        ContractLength length = lengthComboBox.getValue();

        if(lengthComboBox.isDisabled()){

            interestValueField.setText("Nie dotyczy");
        }
        else if(FilterUtils.isStringValueDouble(value) && lengthComboBox.getValue() != null){

            interestValueField.setText(
                    FilterUtils.formatDouble(FilterUtils.calculateInterest(
                            Double.parseDouble(FilterUtils.changeCommaToDot(value)), length)));
        }
        else{

            interestValueField.setText("");
        }
    }


    public static void setDateToField(DatePicker dateFromPicker, TextField dateToField, ComboBox<ContractLength> lengthComboBox){

        LocalDate dateTo = DateUtils.calculateDateTo(lengthComboBox.getValue(), dateFromPicker.getValue());

        dateToField.setText(dateTo == null ? "" : DateUtils.toPolishDateFormat(dateTo));
    }

    public static void disableNonLoanElements(ComboBox<ContractType> typeComboBox, ComboBox<ContractLength> lengthComboBox,
                                              TextField interestValueField, TextField dateToField, Label valueLabel,
                                              DatePicker dateFromPicker, TextField valueTextField, DAO dao) {

        ContractType contractType = typeComboBox.getValue();

        if(contractType != null && contractType.equals(dao.findContractTypeByName("Sprzedaż"))){

            lengthComboBox.setDisable(true);
            interestValueField.setText("Nie dotyczy");
            dateToField.setText("Nie dotyczy");
            valueLabel.setText("*Kwota sprzedaży:");

            dateToField.setStyle("-fx-opacity: 0.4;");
            interestValueField.setStyle("-fx-opacity: 0.4;");
        }
        else{

            lengthComboBox.setDisable(false);
            dateToField.setStyle("-fx-opacity: 1;");
            interestValueField.setStyle("-fx-opacity: 1;");
            valueLabel.setText("*Kwota pożyczki:");
            ElementPropertiesUtils.setDateToField(dateFromPicker, dateToField, lengthComboBox);
            ElementPropertiesUtils.setInterestValueField(valueTextField, interestValueField, lengthComboBox);
        }
    }

    public static void addContractDatePickerListener(DatePicker dateFromPicker, ComboBox<ContractLength> lengthComboBox,
                                                     TextField dateToField){

        dateFromPicker.valueProperty().addListener((ov, oldValue, newValue) -> {

            LocalDate dateTo = DateUtils.calculateDateTo(lengthComboBox.getValue(), dateFromPicker.getValue());

            dateToField.setText(dateTo == null ? "" : DateUtils.toPolishDateFormat(dateTo));
        });
    }

    public static void addContractLengthComboBoxListener(DatePicker dateFromPicker, ComboBox<ContractLength> lengthComboBox,
                                                         TextField dateToField, TextField valueTextField,
                                                         TextField interestValueField){

        lengthComboBox.valueProperty().addListener((ov, oldValue, newValue) -> {

            ElementPropertiesUtils.setDateToField(dateFromPicker, dateToField, lengthComboBox);

            ElementPropertiesUtils.setInterestValueField(valueTextField, interestValueField, lengthComboBox);
        });
    }

    public static void addContractValueFieldListener(TextField valueTextField, TextField interestValueField,
                                                     ComboBox<ContractLength> lengthComboBox){

        valueTextField.textProperty().addListener((ov, oldValue, newValue) ->

                ElementPropertiesUtils.setInterestValueField(valueTextField, interestValueField, lengthComboBox));
    }

    public static void addContractTypeComboBoxListener(ComboBox<ContractType> typeComboBox, TextField valueTextField,
                                                       TextField interestValueField, DatePicker dateFromPicker,
                                                       ComboBox<ContractLength> lengthComboBox, TextField dateToField,
                                                       Label valueLabel, DAO dao){

        typeComboBox.valueProperty().addListener((ov, oldValue, newValue) ->

            ElementPropertiesUtils.disableNonLoanElements(typeComboBox, lengthComboBox, interestValueField,
                    dateToField, valueLabel, dateFromPicker, valueTextField, dao));
    }

    public static void fillContractTableView(ObservableList<Contract> contracts, Label countLabel,
            TableView<Contract> contractsTableView, TableColumn<Contract, String> contractNoColumn,
            TableColumn<Contract, String> contractTypeColumn, TableColumn<Contract, String> customerColumn,
            TableColumn<Contract, String> dateFromColumn, TableColumn<Contract, String> dateToColumn,
            TableColumn<Contract, String> valueColumn, TableColumn<Contract, String> rateColumn,
            TableColumn<Contract, String> productColumn, TableColumn<Contract, String> statusColumn,
            DAO dao){

        contractsTableView.setItems(contracts);

        contractNoColumn.setCellValueFactory(new PropertyValueFactory<>("contractNo"));

        contractTypeColumn.setCellValueFactory(element ->
                new SimpleStringProperty(element.getValue().getContractTypeByTypeId().getType()));

        customerColumn.setCellValueFactory(element -> {

            Customer customer = element.getValue().getCustomerByCustomerId();

            return new SimpleStringProperty(customer.getSurname() + " " + customer.getName());
        });

        dateFromColumn.setCellValueFactory(element -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            return new SimpleStringProperty(formatter.format(element.getValue().getDateFrom()));
        });

        dateToColumn.setCellValueFactory(element -> {

            LocalDate date = element.getValue().getDateTo();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            return new SimpleStringProperty(date == null ? "Nie dotyczy" : formatter.format(date));
        });

        valueColumn.setCellValueFactory(element ->

                new SimpleStringProperty(FilterUtils.formatDouble(element.getValue().getValue())));

        rateColumn.setCellValueFactory(element -> {

            ContractLength contractLength = element.getValue().getContractLengthByLengthId();

            return new SimpleStringProperty(contractLength == null ?
                    "Nie dotyczy" : FilterUtils.formatDouble(FilterUtils.calculateInterest(element.getValue().getValue(), contractLength))
                    + " (" + FilterUtils.formatDouble(contractLength.getStorageRate()) + "% + "
                    + FilterUtils.formatDouble(contractLength.getLoanRate()) + "%)");
        });

        productColumn.setCellValueFactory(element -> {

            Product product = element.getValue().getProductByProductId();

            return new SimpleStringProperty(product.getName()+ " | " + FilterUtils.formatDouble(product.getValue()));
        });

        statusColumn.setCellValueFactory(element ->
                new SimpleStringProperty(element.getValue().getContractStatusByStatusId().getStatus()));

        contractsTableView.setRowFactory(element -> new TableRow<>() {
            @Override
            public void updateItem(Contract item, boolean empty) {

                super.updateItem(item, empty);

                Contract selectedContract = element.getSelectionModel().getSelectedItem();

                if (item == null) {

                    setStyle("");
                }
                else if(selectedContract != null && selectedContract.equals(item)){

                    setStyle("-fx-background-color: -fx-accent");
                }
                else if(item.getContractStatusByStatusId().equals(dao.findContractStatusByName("Nieodebrana"))){

                    setStyle("-fx-background-color: #ffc2b3;");
                }
                else if (DateUtils.doesDateCrossThreshold(item.getDateTo(), DateUtils.CONTRACT_DATE_TO_THRESHOLD)
                            && item.getContractStatusByStatusId().equals(dao.findContractStatusByName("Aktywna"))) {

                    setStyle("-fx-background-color: #ffffb3;");
                }
                else {

                    setStyle("-fx-background-color: #d9ffb3;");
                }
            }
        });

        countLabel.setText("Łącznie: " + contracts.size());
    }

    public static<T> void setTableViewSelectionListener(TableView<T> tableView, Button editButton,
                                                        Button deleteButton){

        editButton.setDisable(true);
        deleteButton.setDisable(true);

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {

                    if(tableView.getSelectionModel().getSelectedItem() == null){

                        editButton.setDisable(true);
                        deleteButton.setDisable(true);
                    }
                    else{

                        editButton.setDisable(false);
                        deleteButton.setDisable(false);
                    }
                });
    }
}
