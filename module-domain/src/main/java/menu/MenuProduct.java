package kitchenpos.domain.menu;

import kitchenpos.ValueObjectId;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class MenuProduct extends ValueObjectId {
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
        return super.getSeq();
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
