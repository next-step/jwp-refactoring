package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public final class GuestNumber {

    private static final Integer MIN_NUMBER_OF_GUESTS = 0;

    @Column(nullable = false)
    private Integer numberOfGuests;

    protected GuestNumber() {
    }

    private GuestNumber(Integer numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static GuestNumber valueOf(Integer numberOfGuests) {
        if (Objects.isNull(numberOfGuests)) {
            numberOfGuests = MIN_NUMBER_OF_GUESTS;
        }
        return new GuestNumber(numberOfGuests);
    }

    public Integer toInteger() {
        return numberOfGuests;
    }

    /**
     * 손님수는 0이상이어야 한다.
     *
     * @param numberOfGuests
     */
    private void validate(Integer numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new InvalidArgumentException(
                String.format("손님의 수는 %s 이상이어야 합니다.", MIN_NUMBER_OF_GUESTS));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GuestNumber that = (GuestNumber) o;
        return numberOfGuests.equals(that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}
