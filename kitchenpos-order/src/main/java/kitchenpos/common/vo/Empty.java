package kitchenpos.common.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Empty {
    public static final Empty EMPTY = Empty.of(true);
    public static final Empty NOT_EMPTY = Empty.of(false);
    @Column(name = "empty", nullable = false)
    private boolean value;

    protected Empty() {}

    private Empty(boolean value) {
        this.value = value;
    }

    public static Empty of(boolean empty) {
        return new Empty(empty);
    }

    public boolean isEmpty() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empty)) {
            return false;
        }
        Empty empty = (Empty)o;
        return value == empty.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
