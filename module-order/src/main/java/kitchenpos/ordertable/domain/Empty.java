package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;

@Embeddable
public class Empty {

    @Column(name = "is_empty", nullable = false)
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

    public void validNotEmpty() {
        if (!isEmpty()) {
            throw new InvalidParameterException(CommonErrorCode.TABLE_NOT_EMPTY_EXCEPTION);
        }
    }
}
