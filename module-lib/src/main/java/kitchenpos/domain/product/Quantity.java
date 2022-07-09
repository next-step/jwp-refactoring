package kitchenpos.domain.product;

import kitchenpos.exception.product.QuantityException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {
    @Column(nullable = false)
    private long quantity;

    protected Quantity() {
    }

    public Quantity(long quantity) {
        quantityValid(quantity);
        this.quantity = quantity;
    }

    private void quantityValid(long quantity) {
        if (Objects.isNull(quantity) || quantity < 0) {
            throw new QuantityException(QuantityException.INVALID_QUANTITY_MSG);
        }
    }

    public long getQuantity() {
        return quantity;
    }
}
