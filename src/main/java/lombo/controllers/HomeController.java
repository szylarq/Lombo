package lombo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombo.DAO.DAO;
import lombo.model.Contract;
import lombo.utils.DateUtils;
import lombo.utils.ElementPropertiesUtils;
import lombo.utils.SceneStageUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    SceneStageUtils utils = new SceneStageUtils();
    private final DAO dao = new DAO();

    @FXML
    private TextField productSearchField;
    @FXML
    private Label todayDateLabel;
    @FXML
    private Label countLabel;
    @FXML
    private TableView<Contract> homeTableView;
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

        ObservableList<Contract> contracts = FXCollections.observableArrayList(dao.getExpiringContracts());

        fillTableView(contracts);

        ElementPropertiesUtils.setStringDateComparator(dateFromColumn);
        ElementPropertiesUtils.setStringDateComparator(dateToColumn);
    }

    @FXML
    private void onProductSearchClicked() throws IOException {

        if(productSearchField.getText().isEmpty()){

            Stage newAlertWindow = SceneStageUtils.getEmptyPopupWindow("Błąd");
            FXMLLoader loader = utils.getLoaderFromFXMLPath(SceneStageUtils.ALERT_BOX_FXML_FILE_PATH);
            newAlertWindow.setScene(new Scene(loader.load()));

            AlertBoxController alertBoxController = loader.getController();

            alertBoxController.setErrorMessage("Szukana nazwa produktu nie może być pusta!");

            newAlertWindow.showAndWait();
        }
        else{

            Desktop desktop = Desktop.getDesktop();

            StringBuilder googleURI = new StringBuilder("https://www.google.com/search?q=");
            StringBuilder olxURI = new StringBuilder("https://www.olx.pl/oferty/q-");
            StringBuilder allegroURI = new StringBuilder("https://allegro.pl/listing?string=");

            for (String word : productSearchField.getText().split(" ")) {

                googleURI.append(word).append("+");
                olxURI.append(word).append("-");
                allegroURI.append(word).append("%20");
            }

            desktop.browse(URI.create(allegroURI.toString()));
            desktop.browse(URI.create(olxURI.toString()));
            desktop.browse(URI.create(googleURI.toString()));
        }
    }

    private void fillTableView(ObservableList<Contract> contracts){

        ElementPropertiesUtils.fillContractTableView(contracts, countLabel, homeTableView, contractNoColumn,
                contractTypeColumn, customerColumn, dateFromColumn, dateToColumn, valueColumn, rateColumn, productColumn,
                statusColumn, dao);
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
    private void onProductsTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.PRODUCTS_FXML_FILE_PATH);
    }
    @FXML
    private void onCategoriesTabClicked(MouseEvent event) throws IOException {

        utils.changeSceneOnTabClick(event, SceneStageUtils.CATEGORIES_FXML_FILE_PATH);
    }
}
