package lombo.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Product {
    private Integer productId;
    private String name;
    private Double value;
    private Collection<Contract> contractsByProductId;
    private ProductCategory productCategoryByCategoryId;
    private ProductStatus productStatusByStatusId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID", nullable = false)
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "NAME", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "VALUE", nullable = false, precision = 0)
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (productId != null ? !productId.equals(product.productId) : product.productId != null) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (value != null ? !value.equals(product.value) : product.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "productByProductId")
    public Collection<Contract> getContractsByProductId() {
        return contractsByProductId;
    }

    public void setContractsByProductId(Collection<Contract> contractsByProductId) {
        this.contractsByProductId = contractsByProductId;
    }

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID")
    public ProductCategory getProductCategoryByCategoryId() {
        return productCategoryByCategoryId;
    }

    public void setProductCategoryByCategoryId(ProductCategory productCategoryByCategoryId) {
        this.productCategoryByCategoryId = productCategoryByCategoryId;
    }

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID", nullable = false)
    public ProductStatus getProductStatusByStatusId() {
        return productStatusByStatusId;
    }

    public void setProductStatusByStatusId(ProductStatus productStatusByStatusId) {
        this.productStatusByStatusId = productStatusByStatusId;
    }
}
