package kitchenpos.domain.menu;

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

    private Long productId;

    private long quantity;

    protected MenuProduct() {
    }

    MenuProduct(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(final Long productId, final Long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public BigDecimal calculateTotalPrice(final BigDecimal productPrice) {
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
