package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderTableExceptionCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class OrderEmpty {
    @Column
    private boolean empty;

    protected OrderEmpty() {}

    public OrderEmpty(boolean empty) {
        this.empty = empty;
    }

    public void validateForTableGrouping() {
        if (!this.empty) {
            throw new IllegalArgumentException(
                    OrderTableExceptionCode.NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
        }
    }

    public OrderEmpty changeEmpty(boolean empty) {
        return new OrderEmpty(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}
