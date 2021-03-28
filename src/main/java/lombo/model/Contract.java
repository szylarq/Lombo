package lombo.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Contract {
    private Integer contractId;
    private String contractNo;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Double value;
    private Integer parentContractId;
    private Customer customerByCustomerId;
    private ContractType contractTypeByTypeId;
    private ContractLength contractLengthByLengthId;
    private Product productByProductId;
    private ContractStatus contractStatusByStatusId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTRACT_ID", nullable = false)
    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    @Basic
    @Column(name = "CONTRACT_NO", nullable = false, length = 17)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Basic
    @Column(name = "DATE_FROM", nullable = false)
    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    @Basic
    @Column(name = "DATE_TO", nullable = true)
    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    @Basic
    @Column(name = "VALUE", nullable = false, precision = 0)
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Basic
    @Column(name = "PARENT_CONTRACT_ID", nullable = true)
    public Integer getParentContractId() {
        return parentContractId;
    }

    public void setParentContractId(Integer parentContractId) {
        this.parentContractId = parentContractId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contract contract = (Contract) o;

        if (contractId != null ? !contractId.equals(contract.contractId) : contract.contractId != null) return false;
        if (contractNo != null ? !contractNo.equals(contract.contractNo) : contract.contractNo != null) return false;
        if (dateFrom != null ? !dateFrom.equals(contract.dateFrom) : contract.dateFrom != null) return false;
        if (dateTo != null ? !dateTo.equals(contract.dateTo) : contract.dateTo != null) return false;
        if (value != null ? !value.equals(contract.value) : contract.value != null) return false;
        if (parentContractId != null ? !parentContractId.equals(contract.parentContractId) : contract.parentContractId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = contractId != null ? contractId.hashCode() : 0;
        result = 31 * result + (contractNo != null ? contractNo.hashCode() : 0);
        result = 31 * result + (dateFrom != null ? dateFrom.hashCode() : 0);
        result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (parentContractId != null ? parentContractId.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID", nullable = false)
    public Customer getCustomerByCustomerId() {
        return customerByCustomerId;
    }

    public void setCustomerByCustomerId(Customer customerByCustomerId) {
        this.customerByCustomerId = customerByCustomerId;
    }

    @ManyToOne
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "TYPE_ID", nullable = false)
    public ContractType getContractTypeByTypeId() {
        return contractTypeByTypeId;
    }

    public void setContractTypeByTypeId(ContractType contractTypeByTypeId) {
        this.contractTypeByTypeId = contractTypeByTypeId;
    }

    @ManyToOne
    @JoinColumn(name = "LENGTH_ID", referencedColumnName = "LENGTH_ID")
    public ContractLength getContractLengthByLengthId() {
        return contractLengthByLengthId;
    }

    public void setContractLengthByLengthId(ContractLength contractLengthByLengthId) {
        this.contractLengthByLengthId = contractLengthByLengthId;
    }

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID", nullable = false)
    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID", nullable = false)
    public ContractStatus getContractStatusByStatusId() {
        return contractStatusByStatusId;
    }

    public void setContractStatusByStatusId(ContractStatus contractStatusByStatusId) {
        this.contractStatusByStatusId = contractStatusByStatusId;
    }
}
