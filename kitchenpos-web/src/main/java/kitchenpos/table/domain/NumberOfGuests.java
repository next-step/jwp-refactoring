package kitchenpos.table.domain;

import kitchenpos.table.exception.IllegalGuestNumberException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * packageName : kitchenpos.domain
 * fileName : NumberOfGuests
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@Embeddable
public class NumberOfGuests {
    @Transient
    public static final Integer MIN_GUESTS = 0;

    @Column(nullable = false)
    private Integer numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(Integer value) {
        validate(value);
        this.numberOfGuests = value;
    }

    private void validate(Integer value) {
        if (Objects.isNull(value) || value < MIN_GUESTS) {
            throw new IllegalGuestNumberException();
        }
    }

    public static NumberOfGuests of(Integer value) {
        return new NumberOfGuests(value);
    }

    public Integer value() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberOfGuests that = (NumberOfGuests) o;
        return numberOfGuests.intValue() == that.numberOfGuests.intValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}
