package lombo.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "CONTRACT_LENGTH", schema = "PUBLIC", catalog = "LOMBO")
public class ContractLength {
    private Integer lengthId;
    private String length;
    private Double loanRate;
    private Double storageRate;
    private Collection<Contract> contractsByLengthId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LENGTH_ID", nullable = false)
    public Integer getLengthId() {
        return lengthId;
    }

    public void setLengthId(Integer lengthId) {
        this.lengthId = lengthId;
    }

    @Basic
    @Column(name = "LENGTH", nullable = false, length = 10)
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Basic
    @Column(name = "LOAN_RATE", nullable = false, precision = 0)
    public Double getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(Double loanRate) {
        this.loanRate = loanRate;
    }

    @Basic
    @Column(name = "STORAGE_RATE", nullable = false, precision = 0)
    public Double getStorageRate() {
        return storageRate;
    }

    public void setStorageRate(Double storageRate) {
        this.storageRate = storageRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractLength that = (ContractLength) o;

        if (lengthId != null ? !lengthId.equals(that.lengthId) : that.lengthId != null) return false;
        if (length != null ? !length.equals(that.length) : that.length != null) return false;
        if (loanRate != null ? !loanRate.equals(that.loanRate) : that.loanRate != null) return false;
        if (storageRate != null ? !storageRate.equals(that.storageRate) : that.storageRate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lengthId != null ? lengthId.hashCode() : 0;
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (loanRate != null ? loanRate.hashCode() : 0);
        result = 31 * result + (storageRate != null ? storageRate.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "contractLengthByLengthId")
    public Collection<Contract> getContractsByLengthId() {
        return contractsByLengthId;
    }

    public void setContractsByLengthId(Collection<Contract> contractsByLengthId) {
        this.contractsByLengthId = contractsByLengthId;
    }
}
