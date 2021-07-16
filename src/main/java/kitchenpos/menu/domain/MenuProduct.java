package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long menuId;

    private Long productId;

    private long quantity;

    protected MenuProduct() {
        // empty
    }

    public MenuProduct(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getQuantity() {
        return this.quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return this.productId;
    }
}
