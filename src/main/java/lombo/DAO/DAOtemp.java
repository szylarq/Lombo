package lombo.DAO;

//import javafx.collections.FXCollections;
//import lombo.model.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;

public class DAOtemp {

//    private List<Product> products = new ArrayList<>();
//    private List<Contract> contracts = new ArrayList<>();
//    private List<Customer> customers = new ArrayList<>();
//    private List<ContractType> contractTypes = new ArrayList<>();
//    private List<ProductCategory> productCategories = new ArrayList<>();
//    private List<ContractLength> contractLengths = new ArrayList<>();
//    private List<ProductStatus> productStatuses = new ArrayList<>();
//    private List<ContractStatus> contractStatuses = new ArrayList<>();
//
//    public DAOtemp(){
//
//        fillCustomersList();
//        fillContractStatusesList();
//        fillProductStatusesList();
//        fillContractTypesList();
//        fillProductCategoriesList();
//        fillContractLengthsList();
//        fillProductsList();
//        fillContractsList();
//    }
//    private void fillCustomersList(){
//
//        customers.add(new Customer(1,"Jan", "Kowalski", "ABC123456",
//                "Stanisław", "Legnicka 1/2", "Prezydent Wrocławia"));
//        customers.add(new Customer(2, "Bożena", "Fraś", "BDB0987654",
//                "Edmund", "Nowodworska 12", "Prezydent Miasta Stołecznego Warszawa"));
//    }
//    private void fillContractsList(){
//
//        contracts = FXCollections.observableArrayList();
//        contracts.add(new Contract(1, "1/2/20", 1, 2,
//                LocalDate.of(2020, 5, 11), LocalDate.of(2020, 5,18), 1, 400.0,
//                1, 1, null));
//        contracts.add(new Contract(2, "2/2/20", 1, 1,
//                LocalDate.of(2020, 3, 28), null,null,
//                220.0, 2, 3, null));
//        contracts.add(new Contract(3, "3/2/20", 2, 2,
//                LocalDate.of(2020, 3, 4),LocalDate.of(2020, 3, 21), 3, 20.0,
//                3, 2, null));
//    }
//    private void fillProductsList(){
//
//        products.add(new Product(1, "LG 24 cale", 400.0, 1, 1));
//        products.add(new Product(2, "Obrączka 5g 585", 200.0, 2, 1));
//        products.add(new Product(3, "Flet", 20.0, 3, 3));
//    }
//    private void fillContractTypesList(){
//
//        contractTypes.add(new ContractType(1, "Sprzedaż"));
//        contractTypes.add(new ContractType(2, "Pożyczka"));
//    }
//    private void fillContractLengthsList(){
//
//        contractLengths.add(new ContractLength(1, "1 tydzień", 0.25, 5.75));
//        contractLengths.add(new ContractLength(2, "2 tygodnie", 0.5, 11.5));
//        contractLengths.add(new ContractLength(3, "3 tygodnie", 0.75, 15.25));
//        contractLengths.add(new ContractLength(4, "1 miesiąc", 1.0, 19.0));
////        contractLengths.add(new ContractLength(5, "Sprzedaż", 0.0,0.0));
//    }
//    private void fillProductCategoriesList(){
//
//        productCategories.add(new ProductCategory(1, "Telewizory"));
//        productCategories.add(new ProductCategory(2, "Złoto"));
//        productCategories.add(new ProductCategory(3, "Instrumenty muzyczne"));
//        productCategories.add(new ProductCategory(4, "Inne"));
//    }
//
//    private void fillProductStatusesList(){
//
//        productStatuses.add(new ProductStatus(1, "Aktywna umowa"));
//        productStatuses.add(new ProductStatus(2, "Sprzedano"));
//        productStatuses.add(new ProductStatus(3, "Nieodebrane"));
//        productStatuses.add(new ProductStatus(4, "Zwrócono"));
//        productStatuses.add(new ProductStatus(5, "Nieznany"));
//    }
//
//    private void fillContractStatusesList(){
//
//        contractStatuses.add(new ContractStatus(1, "Aktywna"));
//        contractStatuses.add(new ContractStatus(2, "Nieodebrana"));
//        contractStatuses.add(new ContractStatus(3, "Zakończona"));
//    }
//
//    public List<Product> getProducts() {
//        return products;
//    }
//
//    public void setProducts(List<Product> products) {
//        this.products = products;
//    }
//
//    public List<Contract> getContracts() {
//        return contracts;
//    }
//
//    public void setContracts(List<Contract> contracts) {
//        this.contracts = contracts;
//    }
//
//    public List<Customer> getCustomers() {
//        return customers;
//    }
//
//    public void setCustomers(List<Customer> customers) {
//        this.customers = customers;
//    }
//
//    public List<ContractType> getContractTypes() {
//        return contractTypes;
//    }
//
//    public void setContractTypes(List<ContractType> contractTypes) {
//        this.contractTypes = contractTypes;
//    }
//
//    public List<ProductCategory> getProductCategories() {
//        return productCategories;
//    }
//
//    public void setProductCategories(List<ProductCategory> productCategories) {
//        this.productCategories = productCategories;
//    }
//
//    public List<ContractLength> getContractLengths() {
//        return contractLengths;
//    }
//
//    public void setContractLengths(List<ContractLength> contractLengths) {
//        this.contractLengths = contractLengths;
//    }
//
//    public List<ProductStatus> getProductStatuses() {
//        return productStatuses;
//    }
//
//    public void setProductStatuses(List<ProductStatus> productStatuses) {
//        this.productStatuses = productStatuses;
//    }
//
//    public List<ContractStatus> getContractStatuses() {
//        return contractStatuses;
//    }
//
//    public void setContractStatuses(List<ContractStatus> contractStatuses) {
//        this.contractStatuses = contractStatuses;
//    }
//
//    public Customer findCustomerById(Customer id) {
//
//        return customers.stream().filter(element -> element.getCustomerId() == id).findFirst().orElse(null);
//    }
//
//    public Contract findContractById(int id) {
//
//        return contracts.stream().filter(element -> element.getContractId() == id).findFirst().orElse(null);
//    }
//
//    public Product findProductById(Product id) {
//
//        return products.stream().filter(element -> element.getProductId() == id).findFirst().orElse(null);
//    }
//
//    public ContractType findContractTypeById(ContractType id) {
//
//        return contractTypes.stream().filter(element -> element.getTypeId() == id).findFirst().orElse(null);
//    }
//
//    public ContractLength findContractLengthById(ContractLength id) {
//
//        return contractLengths.stream().filter(element -> element.getLengthId() == id).findFirst().orElse(null);
//    }
//
//    public ContractStatus findContractStatusById(ContractStatus id){
//
//        return contractStatuses.stream().filter(element -> element.getStatusId() == id).findFirst().orElse(null);
//    }
//
//    public ProductStatus findProductStatusById(ProductStatus id){
//
//        return productStatuses.stream().filter(element -> element.getStatusId() == id).findFirst().orElse(null);
//    }
//
//    public ProductCategory findProductCategoryById(ProductCategory id){
//
//        return productCategories.stream().filter(element -> element.getCategoryId() == id).findFirst().orElse(null);
//    }
}
