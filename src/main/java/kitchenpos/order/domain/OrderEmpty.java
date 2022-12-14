package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.criteria.Order;

@Embeddable
public class OrderEmpty {
    @Column
    private boolean empty;

    protected OrderEmpty() {}

    private OrderEmpty(boolean empty) {
        this.empty = empty;
    }

    public static OrderEmpty of(boolean empty) {
        return new OrderEmpty(empty);
    }

    public void validateForTableGrouping() {
        if (!this.empty) {
            throw new IllegalArgumentException();
        }
    }

    public OrderEmpty changeEmpty(boolean empty) {
        return new OrderEmpty(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}
