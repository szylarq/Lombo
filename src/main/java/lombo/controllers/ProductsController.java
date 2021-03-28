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

public class ProductsController implements Initializable {

    private final SceneStageUtils utils = new SceneStageUtils();
    private final DAO dao = new DAO();

    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<ProductCategory> categoryComboBox;
    @FXML
    private ComboBox<ProductStatus> statusComboBox;
    @FXML
    private TextField valueFromField;
    @FXML
    private TextField valueToField;
    @FXML
    private Label todayDateLabel;
    @FXML
    private Label countLabel;
    @FXML
    private TableView<Product> productsTableView;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> valueColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, String> statusColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        todayDateLabel.setText(DateUtils.getDayOfWeekString(LocalDate.now()));

        ObservableList<Product> products = FXCollections.observableArrayList(dao.getAllProducts());
        fillTableView(products);

        ElementPropertiesUtils.setTableViewSelectionListener(productsTableView, editButton, deleteButton);

        ElementPropertiesUtils.fillCategoryComboBox(FXCollections.observableArrayList(dao.getAllProductCategories()), categoryComboBox);
        ElementPropertiesUtils.fillProductStatusComboBox(FXCollections.observableArrayList(dao.getAllProductStatuses()), statusComboBox);

        ElementPropertiesUtils.setProductNameFieldPromptText(nameField);
        ElementPropertiesUtils.setCategoryComboBoxPromptText(categoryComboBox);
        ElementPropertiesUtils.setProductStatusComboBoxPromptText(statusComboBox);
        ElementPropertiesUtils.setValueFieldPromptText(valueFromField);
        ElementPropertiesUtils.setValueFieldPromptText(valueToField);
    }

    @FXML
    private void onFilterProductsClicked() {

        fillTableView(FXCollections.observableArrayList(productsFilter(dao.getAllProducts())));
    }
    private List<Product> productsFilter(List<Product> products) {

        String name = nameField.getText();
        ProductCategory category = categoryComboBox.getValue();
        ProductStatus status = statusComboBox.getValue();
        String valueFrom = valueFromField.getText();
        String valueTo = valueToField.getText();

        Stream<Product> productsStream = products.stream();

        Map<Predicate<Product>, Boolean> conditionPredicateMap = new HashMap<>();

        conditionPredicateMap.put(element -> element.getName().toLowerCase()
                .contains(name.toLowerCase()), name.length() == 0);

        conditionPredicateMap.put(element -> {

            ProductCategory currentProductCategory = element.getProductCategoryByCategoryId();

            return currentProductCategory != null && currentProductCategory.equals(category);

        }, category == null);

        conditionPredicateMap.put(element -> FilterUtils.isValueWithinRange(
                FilterUtils.changeCommaToDot(valueFrom), FilterUtils.changeCommaToDot(valueTo), element.getValue()),
                !FilterUtils.isStringValueDouble(valueFrom) && !FilterUtils.isStringValueDouble(valueTo));

        conditionPredicateMap.put(element -> element.getProductStatusByStatusId().equals(status), status == null);

        return FilterUtils.filterStreamByPredicates(productsStream, conditionPredicateMap).collect(Collectors.toList());
    }

    private void fillTableView(ObservableList<Product> products){

        productsTableView.setItems(products);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        valueColumn.setCellValueFactory(element ->

                new SimpleStringProperty(FilterUtils.formatDouble(element.getValue().getValue())));

        categoryColumn.setCellValueFactory(element -> {

                ProductCategory productCategory = element.getValue().getProductCategoryByCategoryId();

                return new SimpleStringProperty(productCategory == null ? "Nie skategoryzowano" : productCategory.getName());
        });

        statusColumn.setCellValueFactory(element ->
                new SimpleStringProperty(element.getValue().getProductStatusByStatusId().getStatus()));

        countLabel.setText("Łącznie: " + products.size());
    }

    @FXML
    private void onResetNameFieldClicked() {

        nameField.setText("");
    }
    @FXML
    private void onResetCategoryComboBoxClicked() {

        categoryComboBox.setValue(null);
        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ProductCategory category, boolean isEmpty) {
                super.updateItem(category, isEmpty);

                if (isEmpty || category == null) {
                    setText("Wybierz kategorię");
                }
                else {
                    setText(category.getName());
                }
            }
        });
    }
    @FXML
    private void onResetValueFromFieldClicked() {

        valueFromField.setText("");
    }
    @FXML
    private void onResetValueToFieldClicked() {

        valueToField.setText("");
    }
    @FXML
    private void onResetStatusComboBoxClicked() {

        statusComboBox.setValue(null);
        statusComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ProductStatus status, boolean isEmpty) {
                super.updateItem(status, isEmpty);

                if (isEmpty || status == null) {
                    setText("Wybierz status produktu");
                }
                else {
                    setText(status.getStatus());
                }
            }
        });
    }

    @FXML
    private void onCreateNewProductClicked() throws IOException {

        Product newProduct = utils.getNewProductAndShowWindow();

        if(newProduct != null){

            dao.saveProduct(newProduct);

            onFilterProductsClicked();
        }
    }

    @FXML
    private void onEditProductClicked() throws IOException {

        Stage editProductWindow = SceneStageUtils.getEmptyPopupWindow("Edycja produktu");
        FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.PRODUCT_WINDOW_FXML_FILE_PATH);
        editProductWindow.setScene(new Scene(loader.load()));

        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();

        ProductWindowController productWindowController = loader.getController();

        productWindowController.setTitleLabelText("Edycja produktu");
        productWindowController.setButtonText("Zapisz zmiany");
        productWindowController.setNameFieldText(selectedProduct.getName());
        productWindowController.setCategoryComboBoxValue(selectedProduct.getProductCategoryByCategoryId());
        productWindowController.setValueField(FilterUtils.formatDouble(selectedProduct.getValue()));

        editProductWindow.showAndWait();

        Product product = productWindowController.getNewProduct();

        if(product != null){

            selectedProduct.setName(product.getName());
            selectedProduct.setValue(product.getValue());
            selectedProduct.setProductCategoryByCategoryId(product.getProductCategoryByCategoryId());

            dao.updateProduct(selectedProduct);

            productsTableView.refresh();
        }
    }

    @FXML
    private void onDeleteProductClicked() throws IOException {

        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        Collection<Contract> relatedContracts = selectedProduct.getContractsByProductId();

        if(relatedContracts == null || relatedContracts.isEmpty()) {

            Stage newConfirmationWindow = SceneStageUtils.getEmptyPopupWindow("Ostrzeżenie");
            FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.CONFIRMATION_BOX_FXML_FILE_PATH);
            newConfirmationWindow.setScene(new Scene(loader.load()));

            ConfirmationBoxController confirmationBoxController = loader.getController();

            confirmationBoxController.setWarningMessage("Wybrany produkt zostanie trwale usunięty z bazy danych. Czy chcesz kontynuować?");

            newConfirmationWindow.showAndWait();

            if (confirmationBoxController.getAnswer()) {

                dao.deleteProduct(selectedProduct);

                onFilterProductsClicked();
            }
        }
        else{

            utils.getCustomAlertBox("Nie można usunąć, ponieważ w bazie danych istnieją powiązane umowy!")
                    .showAndWait();
        }
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
    private void onCustomersTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.CUSTOMERS_FXML_FILE_PATH);
    }
    @FXML
    private void onCategoriesTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.CATEGORIES_FXML_FILE_PATH);
    }
}
