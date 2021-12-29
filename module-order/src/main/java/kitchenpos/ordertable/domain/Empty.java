package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.OrderErrorCode;
import kitchenpos.exception.InvalidParameterException;

@Embeddable
public class Empty {

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected Empty() {
    }

    private Empty(boolean empty) {
        this.empty = empty;
    }

    public static Empty of(boolean empty) {
        return new Empty(empty);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void validateNotEmpty() {
        if (!isEmpty()) {
            throw new InvalidParameterException(OrderErrorCode.TABLE_NOT_EMPTY_EXCEPTION);
        }
    }
}
