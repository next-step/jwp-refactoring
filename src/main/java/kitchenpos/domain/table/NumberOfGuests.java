package kitchenpos.domain.table;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final String INVALID_NUMBER_OF_GUESTS = "NumberOfGuests 는 0 이상의 숫자로 생성할 수 있습니다.";

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {}

    private NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        return new NumberOfGuests(numberOfGuests);
    }

    private static void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(INVALID_NUMBER_OF_GUESTS);
        }
    }

    public int getValue() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NumberOfGuests that = (NumberOfGuests)o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}
