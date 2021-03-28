package lombo.controllers;

import javafx.beans.property.SimpleIntegerProperty;
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
import lombo.model.Product;
import lombo.model.ProductCategory;
import lombo.utils.DateUtils;
import lombo.utils.ElementPropertiesUtils;
import lombo.utils.FilterUtils;
import lombo.utils.SceneStageUtils;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProductCategoriesController implements Initializable {

    private final SceneStageUtils utils = new SceneStageUtils();
    private final DAO dao = new DAO();

    @FXML
    private TextField nameField;
    @FXML
    private Label todayDateLabel;
    @FXML
    private Label countLabel;
    @FXML
    private TableView<ProductCategory> categoriesTableView;
    @FXML
    private TableColumn<ProductCategory, String> nameColumn;
    @FXML
    private TableColumn<ProductCategory, Integer> numberOfProductsColumn;
    @FXML
    private Button editCategoryButton;
    @FXML
    private Button deleteCategoryButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        todayDateLabel.setText(DateUtils.getDayOfWeekString(LocalDate.now()));

        ObservableList<ProductCategory> categories = FXCollections.observableArrayList(dao.getAllProductCategories());

        fillTableView(categories);

        ElementPropertiesUtils.setTableViewSelectionListener(categoriesTableView, editCategoryButton, deleteCategoryButton);
    }

    @FXML
    private void onFilterCategoriesClicked() {

        fillTableView(FXCollections.observableArrayList(categoriesFilter(dao.getAllProductCategories())));
    }

    private List<ProductCategory> categoriesFilter(List<ProductCategory> categories) {

        String name = nameField.getText();

        return FilterUtils.skipOrFilter(categories.stream(), name.length() == 0,
                element -> element.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    private void fillTableView(ObservableList<ProductCategory> categories){

        categoriesTableView.setItems(categories);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        countLabel.setText("Łącznie: " + categories.size());

        numberOfProductsColumn.setCellValueFactory(element -> {

            List<Product> relatedProducts = (List<Product>) element.getValue().getProductsByCategoryId();

           return new SimpleIntegerProperty(
                   element.getValue().getProductsByCategoryId() == null ? 0 : relatedProducts.size()).asObject();

        });
    }

    @FXML
    private void onResetNameFieldClicked() {

        nameField.setText("");
    }
    @FXML
    private void onCreateNewCategoryClicked() throws IOException {

        ProductCategory newProductCategory = utils.getNewCategoryAndShowWindow();

        if(newProductCategory != null){

            dao.saveProductCategory(newProductCategory);

            onFilterCategoriesClicked();
        }
    }

    @FXML
    private void onEditCategoryClicked() throws IOException {

        ProductCategory selectedCategory = categoriesTableView.getSelectionModel().getSelectedItem();

        Stage editCategoryWindow = SceneStageUtils.getEmptyPopupWindow("Edycja kategorii");
        FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.CATEGORY_WINDOW_FXML_FILE_PATH);
        editCategoryWindow.setScene(new Scene(loader.load()));

        ProductCategoryWindowController productCategoryWindowController = loader.getController();

        productCategoryWindowController.setTitleLabelText("Edycja kategorii");
        productCategoryWindowController.setButtonText("Zapisz zmiany");
        productCategoryWindowController.setNameFieldText(selectedCategory.getName());

        editCategoryWindow.showAndWait();

        ProductCategory productCategory = productCategoryWindowController.getNewCategory();

        if(productCategory != null) {

            selectedCategory.setName(productCategory.getName());

            dao.updateProductCategory(selectedCategory);

            categoriesTableView.refresh();
        }
    }

    @FXML
    private void onDeleteCategoryClicked() throws IOException {

        ProductCategory selectedCategory = categoriesTableView.getSelectionModel().getSelectedItem();
        Collection<Product> relatedProducts = selectedCategory.getProductsByCategoryId();

        if(relatedProducts == null || relatedProducts.isEmpty()) {

            Stage newConfirmationWindow = SceneStageUtils.getEmptyPopupWindow("Ostrzeżenie");
            FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.CONFIRMATION_BOX_FXML_FILE_PATH);
            newConfirmationWindow.setScene(new Scene(loader.load()));

            ConfirmationBoxController confirmationBoxController = loader.getController();

            confirmationBoxController.setWarningMessage("Wybrana kategoria produktów zostanie trwale usunięta z bazy danych. Czy chcesz kontynuować?");

            newConfirmationWindow.showAndWait();

            if (confirmationBoxController.getAnswer()) {

                dao.deleteProductCategory(selectedCategory);

                onFilterCategoriesClicked();
            }
        }
        else{

            utils.getCustomAlertBox("Nie można usunąć, ponieważ w bazie danych istnieją powiązane produkty!")
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
    private void onProductsTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.PRODUCTS_FXML_FILE_PATH);
    }
    @FXML
    private void onCustomersTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.CUSTOMERS_FXML_FILE_PATH);
    }
}
