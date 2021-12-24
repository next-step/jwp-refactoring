package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Empty {

    @Column
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

    public boolean isNotEmpty() {
        return !empty;
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
}
