package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Empty {
    @Column(name = "empty")
    private boolean empty = false;

    public Empty() {
    }

    public Empty(boolean empty) {
        this.empty = empty;
    }

    public boolean value() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empty that = (Empty) o;
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
