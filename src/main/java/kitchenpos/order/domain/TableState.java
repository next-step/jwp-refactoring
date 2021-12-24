package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class TableState {
    private boolean empty;

    protected TableState() {
    }

    public TableState(boolean empty) {
        this.empty = empty;
    }

    private void setEmpty(boolean empty) {
        this.empty = empty;
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
        TableState that = (TableState) o;
        return empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
