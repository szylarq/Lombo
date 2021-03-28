package lombo;

import javafx.application.Application;
import javafx.stage.Stage;
import lombo.DAO.DAO;
import lombo.model.Contract;
import lombo.model.Product;
import lombo.utils.SceneStageUtils;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main extends Application {

    private static final SessionFactory ourSessionFactory;
    private final SceneStageUtils utils = new SceneStageUtils();
    private final DAO dao = new DAO();

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    @Override
    public void start(Stage mainWindow) throws Exception{

        setExpiredContractsAndProductsStatuses();

        mainWindow.setTitle("Lombo");
        mainWindow.setScene(utils.loadSceneFromFXML(SceneStageUtils.HOME_FXML_FILE_PATH));
        mainWindow.setMaximized(true);
        mainWindow.show();
    }

    public static void main(String[] args) {

        launch(args);
    }

    private void setExpiredContractsAndProductsStatuses(){

        List<Contract> expiredContracts = dao.getExpiredContracts();
        Product relatedProduct;

        for (Contract contract : expiredContracts) {

            contract.setContractStatusByStatusId(dao.findContractStatusByName("Nieodebrana"));
            dao.updateContract(contract);

            relatedProduct = contract.getProductByProductId();

            relatedProduct.setProductStatusByStatusId(dao.findProductStatusByName("Nieodebrano"));
            dao.updateProduct(relatedProduct);
        }
    }
}
