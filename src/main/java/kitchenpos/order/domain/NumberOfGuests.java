package kitchenpos.order.domain;


import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    private static final long MIN = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {}

    public NumberOfGuests(int numberOfGuests) {
        validateNotUnderValue(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private static void validateNotUnderValue(int numberOfGuests) {
        if (numberOfGuests < MIN) {
            throw new IllegalArgumentException("손님은 " + MIN + "명 이상이어야 합니다.");
        }
    }

    public int value() {
        return numberOfGuests;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NumberOfGuests)) {
            return false;
        }
        NumberOfGuests that = (NumberOfGuests) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}
