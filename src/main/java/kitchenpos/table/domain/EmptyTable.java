package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public final class EmptyTable {
    @Column(nullable = false)
    private Boolean empty;

    protected EmptyTable() {
    }

    private EmptyTable(Boolean empty) {
        validate(empty);
        this.empty = empty;
    }

    public static EmptyTable valueOf(Boolean empty) {
        return new EmptyTable(empty);
    }

    public Boolean isEmpty() {
        return empty.booleanValue();
    }

    private void validate(Boolean empty) {
        if (Objects.isNull(empty)) {
            throw new InvalidArgumentException("테이블의 상태는 필수입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Boolean) {
            return empty.equals(o);
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmptyTable other = (EmptyTable) o;
        return empty.equals(other.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
