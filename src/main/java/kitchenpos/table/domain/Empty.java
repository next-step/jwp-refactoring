package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class OrderTableEmpty {
    @Column(name = "empty")
    private final boolean empty;

    public OrderTableEmpty() {
        this(false);
    }

    public OrderTableEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean value() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableEmpty that = (OrderTableEmpty) o;
        return empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }

    @Override
    public String toString() {
        return "OrderTableStatus{" +
                "empty=" + empty +
                '}';
    }
}
