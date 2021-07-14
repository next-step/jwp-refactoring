package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import kitchenpos.common.Price;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long menuId;

    private Long productId;

    private long quantity;

    @Transient
    private Price productPrice;

    protected MenuProduct() {
        // empty
    }

    public MenuProduct(final Long productId, final Price productPrice, final long quantity) {
        this.productId = productId;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public Price calculatePrice() {
        return this.productPrice.multiply(this.quantity);
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
