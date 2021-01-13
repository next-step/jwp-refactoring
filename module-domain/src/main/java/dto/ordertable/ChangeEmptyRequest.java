package dto.ordertable;

import java.util.Objects;

public class ChangeEmptyRequest {
    private boolean empty;

    public ChangeEmptyRequest() {
    }

    public ChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ChangeEmptyRequest that = (ChangeEmptyRequest) o;
        return empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }

    @Override
    public String toString() {
        return "ChangeEmptyRequest{" +
                "empty=" + empty +
                '}';
    }
}
