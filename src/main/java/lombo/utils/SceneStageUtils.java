package lombo.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import lombo.controllers.AlertBoxController;
import lombo.controllers.ProductCategoryWindowController;
import lombo.controllers.CustomerWindowController;
import lombo.controllers.ProductWindowController;
import lombo.model.Customer;
import lombo.model.Product;
import lombo.model.ProductCategory;

import java.io.IOException;

public class SceneStageUtils {

    public static final String HOME_FXML_FILE_PATH = "/scenes/home.fxml";
    public static final String CONTRACTS_FXML_FILE_PATH = "/scenes/contracts.fxml";
    public static final String CUSTOMERS_FXML_FILE_PATH = "/scenes/customers.fxml";
    public static final String PRODUCTS_FXML_FILE_PATH = "/scenes/products.fxml";
    public static final String CATEGORIES_FXML_FILE_PATH = "/scenes/categories.fxml";
    public static final String CONTRACT_WINDOW_FXML_FILE_PATH = "/scenes/contract_window.fxml";
    public static final String CUSTOMER_WINDOW_FXML_FILE_PATH = "/scenes/customer_window.fxml";
    public static final String PRODUCT_WINDOW_FXML_FILE_PATH = "/scenes/product_window.fxml";
    public static final String CATEGORY_WINDOW_FXML_FILE_PATH = "/scenes/category_window.fxml";
    public static final String ALERT_BOX_FXML_FILE_PATH = "/scenes/alert_box.fxml";
    public static final String CONFIRMATION_BOX_FXML_FILE_PATH = "/scenes/confirmation_box.fxml";
    public static final String PROLONG_CONTRACT_FILE_PATH = "/scenes/prolong_contract_window.fxml";

    public Scene loadSceneFromFXML(String filePath) throws IOException {

        return new Scene(FXMLLoader.load(getClass().getResource(filePath)));
    }

    public Stage getNewPopupWindow(String fxmlFilePath, String title) throws IOException {

        Stage newPopupWindow = new Stage();
        newPopupWindow.setScene(loadSceneFromFXML(fxmlFilePath));
        newPopupWindow.initModality(Modality.APPLICATION_MODAL);
        newPopupWindow.setTitle(title);
        newPopupWindow.setResizable(false);

        return newPopupWindow;
    }

    public static Stage getEmptyPopupWindow(String title){

        Stage newPopupWindow = new Stage();
        newPopupWindow.initModality(Modality.APPLICATION_MODAL);
        newPopupWindow.setTitle(title);
        newPopupWindow.setResizable(false);

        return newPopupWindow;
    }

    public FXMLLoader getLoaderFromFXMLPath(String filePath){

        return new FXMLLoader(getClass().getResource(filePath));
    }

    public void changeSceneOnTabClick(MouseEvent event, String filePath) throws IOException {

        Stage mainWindow = getSourceStage(event);
        mainWindow.setScene(loadSceneFromFXML(filePath));
        mainWindow.show();
    }

    public static Stage getSourceStage(MouseEvent event){

        return (Stage)((Node)event.getSource()).getScene().getWindow();
    }

    public Stage getDefaultAlertBox() throws IOException {

        return getNewPopupWindow(SceneStageUtils.ALERT_BOX_FXML_FILE_PATH, "Błąd");
    }

    public ProductCategory getNewCategoryAndShowWindow() throws IOException {

        Stage newCategoryWindow = SceneStageUtils.getEmptyPopupWindow("Nowa kategoria");
        FXMLLoader loader = getLoaderFromFXMLPath(SceneStageUtils.CATEGORY_WINDOW_FXML_FILE_PATH);
        newCategoryWindow.setScene(new Scene(loader.load()));

        ProductCategoryWindowController productCategoryWindowController = loader.getController();

        newCategoryWindow.showAndWait();

        return productCategoryWindowController.getNewCategory();
    }

    public Product getNewProductAndShowWindow() throws IOException {

        Stage newProductWindow = SceneStageUtils.getEmptyPopupWindow("Nowy produkt");
        FXMLLoader loader = getLoaderFromFXMLPath(SceneStageUtils.PRODUCT_WINDOW_FXML_FILE_PATH);
        newProductWindow.setScene(new Scene(loader.load()));

        ProductWindowController productWindowController = loader.getController();

        newProductWindow.showAndWait();

        return productWindowController.getNewProduct();
    }

    public Customer getNewCustomerAndShowWindow() throws IOException {

        Stage newCustomerWindow = SceneStageUtils.getEmptyPopupWindow("Nowy klient");
        FXMLLoader loader = getLoaderFromFXMLPath(SceneStageUtils.CUSTOMER_WINDOW_FXML_FILE_PATH);
        newCustomerWindow.setScene(new Scene(loader.load()));

        CustomerWindowController customerWindowController = loader.getController();

        newCustomerWindow.showAndWait();

        return customerWindowController.getNewCustomer();
    }

    public Stage getCustomAlertBox(String message) throws IOException {

        Stage newAlertWindow = SceneStageUtils.getEmptyPopupWindow("Błąd");
        FXMLLoader loader = getLoaderFromFXMLPath(SceneStageUtils.ALERT_BOX_FXML_FILE_PATH);
        newAlertWindow.setScene(new Scene(loader.load()));

        AlertBoxController alertBoxController = loader.getController();

        alertBoxController.setErrorMessage(message);

        return newAlertWindow;
    }
}
