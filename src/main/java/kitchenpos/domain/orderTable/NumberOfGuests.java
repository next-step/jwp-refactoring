package kitchenpos.domain.orderTable;

import kitchenpos.domain.orderTable.exceptions.InvalidNumberOfGuestsException;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {
    @Transient
    private static final int MIN_VALUE = 0;

    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < MIN_VALUE) {
            throw new InvalidNumberOfGuestsException("방문한 손님수는 0명 미만일 수 없습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NumberOfGuests that = (NumberOfGuests) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "NumberOfGuests{" +
                "value=" + value +
                '}';
    }
}
