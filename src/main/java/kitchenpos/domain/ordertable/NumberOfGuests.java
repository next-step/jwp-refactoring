package kitchenpos.domain.ordertable;

import kitchenpos.domain.ordertable.exceptions.InvalidNumberOfGuestsException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {
    @Transient
    private static final int MIN_VALUE = 0;

    @Column(name = "number_of_guests")
    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(final int value) {
        validate(value);
        this.value = value;
    }

    public int getValue() {
        return value;
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
