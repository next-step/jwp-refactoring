package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductId {

    @Column(name = "product_id", nullable = false, updatable = false)
    private Long id;

    public ProductId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    protected ProductId(){};

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductId productId = (ProductId) o;
        return Objects.equals(id, productId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
