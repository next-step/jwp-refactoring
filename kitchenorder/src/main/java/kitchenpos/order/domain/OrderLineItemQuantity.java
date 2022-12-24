package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.orderconstants.OrderErrorMessages;

@Embeddable
public class OrderLineItemQuantity {

    @Column(name = "quantity", nullable = false)
    private long quantity;

    public OrderLineItemQuantity() {}

    public OrderLineItemQuantity(long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(OrderErrorMessages.ORDER_LINE_ITEM_QUANTITY_BELOW_ZERO);
        }
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItemQuantity that = (OrderLineItemQuantity) o;
        return quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
