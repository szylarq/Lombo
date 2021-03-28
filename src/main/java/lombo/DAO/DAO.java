package lombo.DAO;

import lombo.Main;
import lombo.model.*;
import lombo.utils.DateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.util.List;

public class DAO {

    private final Session session = Main.getSession();

    public List<Contract> getAllContracts(){

        Query<Contract> query = session.createQuery("FROM Contract c ORDER BY c.dateFrom DESC");

        return query.list();
    }

    public Contract findContractById(Integer id){

        Transaction transaction = session.beginTransaction();

        Contract contract = session.get(Contract.class, id);

        transaction.commit();

        return contract;
    }

    public void saveContract(Contract newContract){

        Transaction transaction = session.beginTransaction();

        session.persist(newContract);

        transaction.commit();
    }

    public void deleteContract(Contract contract){

        Transaction transaction = session.beginTransaction();

        session.delete(contract);

        transaction.commit();
    }

    public void updateContract(Contract contract){

        Transaction transaction = session.beginTransaction();

        session.merge(contract);

        transaction.commit();
    }

    public List<Contract> getExpiredContracts(){

        LocalDate today = LocalDate.now();

        ContractStatus contractStatus = findContractStatusByName("Aktywna");

        Query<Contract> query = session.createQuery("FROM Contract c " +
                "WHERE c.dateTo < :today AND c.contractStatusByStatusId = :contractStatus " +
                "ORDER BY c.dateTo DESC");

        query.setParameter("today", today);
        query.setParameter("contractStatus", contractStatus);

        return query.list();
    }

    public List<Contract> getExpiringContracts(){

        LocalDate today = LocalDate.now();
        LocalDate thresholdDate = today.plusDays(DateUtils.CONTRACT_DATE_TO_THRESHOLD);
        ContractStatus contractStatus = findContractStatusByName("Aktywna");

        Query<Contract> query = session.createQuery("FROM Contract c " +
                "WHERE c.dateTo >= :today AND c.dateTo <= :thresholdDate " +
                "AND c.contractStatusByStatusId = :contractStatus ORDER BY c.dateTo DESC");

        query.setParameter("thresholdDate", thresholdDate);
        query.setParameter("today", today);
        query.setParameter("contractStatus", contractStatus);

        return query.list();
    }

    public List<Customer> getAllCustomers(){

        Query<Customer> query = session.createQuery("FROM Customer c ORDER BY c.surname, c.name");

        return query.list();
    }

    public Customer findCustomerById(Integer id){

        Transaction transaction = session.beginTransaction();

        Customer customer = session.get(Customer.class, id);

        transaction.commit();

        return customer;
    }

    public void saveCustomer(Customer newCustomer){

        Transaction transaction = session.beginTransaction();

        session.persist(newCustomer);

        transaction.commit();
    }

    public void updateCustomer(Customer customer){

        Transaction transaction = session.beginTransaction();

        session.merge(customer);

        transaction.commit();

    }

    public void deleteCustomer(Customer customer){

        Transaction transaction = session.beginTransaction();

        session.delete(customer);

        transaction.commit();
    }

    public List<Product> getAllProducts(){

        Query<Product> query = session.createQuery("FROM Product p ORDER BY p.name");

        return query.list();
    }

    public Product findProductById(Integer id){

        Transaction transaction = session.beginTransaction();

        Product product = session.get(Product.class, id);

        transaction.commit();

        return product;
    }

    public void saveProduct(Product newProduct){

        Transaction transaction = session.beginTransaction();

        session.persist(newProduct);

        transaction.commit();
    }

    public void updateProduct(Product product){

        Transaction transaction = session.beginTransaction();

        session.merge(product);

        transaction.commit();

    }

    public void deleteProduct(Product product){

        Transaction transaction = session.beginTransaction();

        session.delete(product);

        transaction.commit();
    }

    public List<ContractLength> getAllContractLengths(){

        Query<ContractLength> query = session.createQuery("FROM ContractLength");

        return query.list();
    }

    public ContractLength findContractLengthById(Integer id){

        Transaction transaction = session.beginTransaction();

        ContractLength contractLength = session.get(ContractLength.class, id);

        transaction.commit();

        return contractLength;
    }

    public List<ContractType> getAllContractTypes(){

        Query<ContractType> query = session.createQuery("FROM ContractType");

        return query.list();
    }

    public ContractType findContractTypeById(Integer id){

        Transaction transaction = session.beginTransaction();

        ContractType contractType = session.get(ContractType.class, id);

        transaction.commit();

        return contractType;
    }

    public ContractType findContractTypeByName(String name){

        Query<ContractType> query = session.createQuery("FROM ContractType ct " +
                "WHERE LOWER(ct.type) = :name");
        query.setParameter("name", name.toLowerCase());

        return query.uniqueResult();
    }

    public List<ContractStatus> getAllContractStatuses(){

        Query<ContractStatus> query = session.createQuery("FROM ContractStatus");

        return query.list();
    }

    public ContractStatus findContractStatusById(Integer id){

        Transaction transaction = session.beginTransaction();

        ContractStatus contractStatus = session.get(ContractStatus.class, id);

        transaction.commit();

        return contractStatus;
    }

    public ContractStatus findContractStatusByName(String name){

        Query<ContractStatus> query = session.createQuery("FROM ContractStatus cs " +
                "WHERE LOWER(cs.status) = :name");
        query.setParameter("name", name.toLowerCase());

        return query.uniqueResult();
    }


    public List<ProductCategory> getAllProductCategories(){

        Query<ProductCategory> query = session.createQuery("FROM ProductCategory pc ORDER BY pc.name");

        return query.list();
    }

    public ProductCategory findProductCategoryById(Integer id){

        Transaction transaction = session.beginTransaction();

        ProductCategory productCategory = session.get(ProductCategory.class, id);

        transaction.commit();

        return productCategory;
    }

    public ProductCategory findProductCategoryByName(String name){

        Query<ProductCategory> query = session.createQuery("FROM ProductCategory pc " +
                "WHERE LOWER(pc.name) = :name");
        query.setParameter("name", name);

        return query.uniqueResult();
    }

    public void saveProductCategory(ProductCategory newProductCategory){

        Transaction transaction = session.beginTransaction();

        session.persist(newProductCategory);

        transaction.commit();
    }

    public void updateProductCategory(ProductCategory productCategory){

        Transaction transaction = session.beginTransaction();

        session.merge(productCategory);

        transaction.commit();
    }
    public void deleteProductCategory(ProductCategory productCategory){

        Transaction transaction = session.beginTransaction();

        session.delete(productCategory);

        transaction.commit();
    }

    public List<ProductStatus> getAllProductStatuses(){

        Query<ProductStatus> query = session.createQuery("FROM ProductStatus");

        return query.list();
    }

    public ProductStatus findProductStatusById(Integer id){

        Transaction transaction = session.beginTransaction();

        ProductStatus productStatus = session.get(ProductStatus.class, id);

        transaction.commit();

        return productStatus;
    }

    public ProductStatus findProductStatusByName(String name){

        Query<ProductStatus> query = session.createQuery("FROM ProductStatus ps " +
                "WHERE LOWER(ps.status) = :name");
        query.setParameter("name", name.toLowerCase());

        return query.uniqueResult();
    }
}
