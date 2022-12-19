package kitchenpos.order.domain;

import kitchenpos.exception.ErrorMessage;

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
            throw new IllegalArgumentException(ErrorMessage.ORDER_TABLE_NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
        }
    }

    public OrderEmpty changeEmpty(boolean empty) {
        return new OrderEmpty(empty);
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEmpty that = (OrderEmpty) o;
        return empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
