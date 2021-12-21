package kitchenpos.table.domain;

import java.util.Objects;

public class EmptyStatus {
    private boolean empty;

    protected EmptyStatus() {
    }

    public EmptyStatus(boolean empty) {
        this.empty = empty;
    }

    public boolean getStatus() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmptyStatus that = (EmptyStatus) o;
        return empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
