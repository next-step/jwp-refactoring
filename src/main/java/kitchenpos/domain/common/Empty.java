package kitchenpos.domain.common;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Empty {

    @Column(nullable = false)
    private boolean empty;

    protected Empty() {
    }

    public Empty(boolean empty) {
        this.empty = empty;
    }

    public boolean isTrue() {
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
        return empty == empty1.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
