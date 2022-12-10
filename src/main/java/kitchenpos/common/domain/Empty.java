package kitchenpos.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Empty {

    public static final Empty IS_EMPTY = new Empty(true);
    public static final Empty IS_NOT_EMPTY = new Empty(false);

    @Column(nullable = false)
    private boolean empty;

    protected Empty() {}

    private Empty(boolean empty) {
        this.empty = empty;
    }

    public static Empty valueOf(boolean empty) {
        if(empty) {
            return IS_EMPTY;
        }
        return IS_NOT_EMPTY;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Empty empty1 = (Empty) o;
        return isEmpty() == empty1.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isEmpty());
    }
}
