package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.exception.TableExceptionConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderTableEmpty {
    @Column
    private boolean empty;

    protected OrderTableEmpty() {}

    public OrderTableEmpty(boolean empty) {
        this.empty = empty;
    }

    public void validateForTableGrouping() {
        if (!this.empty) {
            throw new IllegalArgumentException(
                    TableExceptionConstants.NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getErrorMessage());
        }
    }

    public OrderTableEmpty changeEmpty(boolean empty) {
        return new OrderTableEmpty(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}
