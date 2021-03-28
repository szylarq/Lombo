package lombo.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Customer {
    private Integer customerId;
    private String name;
    private String surname;
    private String personalIdNo;
    private String fatherName;
    private String address;
    private String issuedBy;
    private Collection<Contract> contractsByCustomerId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID", nullable = false)
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @Basic
    @Column(name = "NAME", nullable = false, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "SURNAME", nullable = false, length = 50)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "PERSONAL_ID_NO", nullable = true, length = 9)
    public String getPersonalIdNo() {
        return personalIdNo;
    }

    public void setPersonalIdNo(String personalIdNo) {
        this.personalIdNo = personalIdNo;
    }

    @Basic
    @Column(name = "FATHER_NAME", nullable = true, length = 30)
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Basic
    @Column(name = "ADDRESS", nullable = true, length = 100)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "ISSUED_BY", nullable = true, length = 50)
    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (customerId != null ? !customerId.equals(customer.customerId) : customer.customerId != null) return false;
        if (name != null ? !name.equals(customer.name) : customer.name != null) return false;
        if (surname != null ? !surname.equals(customer.surname) : customer.surname != null) return false;
        if (personalIdNo != null ? !personalIdNo.equals(customer.personalIdNo) : customer.personalIdNo != null)
            return false;
        if (fatherName != null ? !fatherName.equals(customer.fatherName) : customer.fatherName != null) return false;
        if (address != null ? !address.equals(customer.address) : customer.address != null) return false;
        if (issuedBy != null ? !issuedBy.equals(customer.issuedBy) : customer.issuedBy != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = customerId != null ? customerId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (personalIdNo != null ? personalIdNo.hashCode() : 0);
        result = 31 * result + (fatherName != null ? fatherName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (issuedBy != null ? issuedBy.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "customerByCustomerId")
    public Collection<Contract> getContractsByCustomerId() {
        return contractsByCustomerId;
    }

    public void setContractsByCustomerId(Collection<Contract> contractsByCustomerId) {
        this.contractsByCustomerId = contractsByCustomerId;
    }
}
