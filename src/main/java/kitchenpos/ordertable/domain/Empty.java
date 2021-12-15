package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Empty {

    public static final String MESSAGE_VALIDATE_CHANGABLE = "비어있지 않은 상태여야 합니다.";

    @Column
    private boolean empty;

    protected Empty() {
    }

    public Empty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empty empty1 = (Empty) o;
        return empty == empty1.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }

    boolean isGroupable() {
        return empty;
    }

    void validateNumberOfGuestsChangable() {
        if (empty) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_CHANGABLE);
        }
    }
}
