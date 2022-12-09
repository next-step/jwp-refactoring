package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.constant.ErrorCode;

@Embeddable
public class NumberOfGuests {

    private static final int ZERO = 0;

    @Column(nullable = false, columnDefinition = "INT(11)")
    private int numberOfGuests;

    protected NumberOfGuests() {}

    private NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuestsIsSmallerThanZero(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void validateNumberOfGuestsIsSmallerThanZero(int numberOfGuests) {
        if(numberOfGuests < ZERO) {
            throw new IllegalArgumentException(ErrorCode.방문한_손님_수는_0보다_작을_수_없음.getErrorMessage());
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
        if (o == null || getClass() != o.getClass()) {
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
