package kitchenpos.tobe.orders.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Empty {

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected Empty() {
    }

    public Empty(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isTaken() {
        return !this.empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Empty empty1 = (Empty) o;
        return empty == empty1.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
