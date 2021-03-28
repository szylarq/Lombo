package lombo.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "CONTRACT_STATUS", schema = "PUBLIC", catalog = "LOMBO")
public class ContractStatus {
    private Integer statusId;
    private String status;
    private Collection<Contract> contractsByStatusId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATUS_ID", nullable = false)
    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    @Basic
    @Column(name = "STATUS", nullable = false, length = 30)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractStatus that = (ContractStatus) o;

        if (statusId != null ? !statusId.equals(that.statusId) : that.statusId != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = statusId != null ? statusId.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "contractStatusByStatusId")
    public Collection<Contract> getContractsByStatusId() {
        return contractsByStatusId;
    }

    public void setContractsByStatusId(Collection<Contract> contractsByStatusId) {
        this.contractsByStatusId = contractsByStatusId;
    }
}
