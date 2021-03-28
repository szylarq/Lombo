package lombo.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "CONTRACT_TYPE", schema = "PUBLIC", catalog = "LOMBO")
public class ContractType {
    private Integer typeId;
    private String type;
    private Collection<Contract> contractsByTypeId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TYPE_ID", nullable = false)
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @Basic
    @Column(name = "TYPE", nullable = false, length = 30)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractType that = (ContractType) o;

        if (typeId != null ? !typeId.equals(that.typeId) : that.typeId != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = typeId != null ? typeId.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "contractTypeByTypeId")
    public Collection<Contract> getContractsByTypeId() {
        return contractsByTypeId;
    }

    public void setContractsByTypeId(Collection<Contract> contractsByTypeId) {
        this.contractsByTypeId = contractsByTypeId;
    }
}
