package kitchenpos.core.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(nullable = false, updatable = false)
    private Long productId;
    @Embedded
    private MenuProductQuantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(long productId, long quantity) {
        this.productId = productId;
        this.quantity = MenuProductQuantity.of(quantity);
    }

    private MenuProduct(long seq, long productId, int quantity) {
        this(productId, quantity);
        this.seq = seq;
    }

    public static MenuProduct of(long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct generate(long seq, long productId, int quantity) {
        return new MenuProduct(seq, productId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }


    public BigDecimal totalPrice(BigDecimal productPrice) {
        return productPrice.multiply(quantity.bigDecimalValue());
    }
}
