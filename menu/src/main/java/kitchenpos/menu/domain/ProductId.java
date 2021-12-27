package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductId {
    @Column(name = "product_id")
    private final Long id;

    protected ProductId() {
        this.id = null;
    }

    private ProductId(Long id) {
        this.id = id;
    }

    public static ProductId of(Long id) {
        return new ProductId(id);
    }

    public Long value() {
        return this.id;
    }
}
