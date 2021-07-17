package kitchenpos.table.domain;

import kitchenpos.exception.IllegalNumberOfGuestsException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {

    public static final String NEGATIVE_NUMBER_ERROR_MESSAGE = "손님 수는 음수를 입력할 수 없습니다.";
    private int numberOfGuests;

    public NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        checkIsNegative(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    private void checkIsNegative(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalNumberOfGuestsException(NEGATIVE_NUMBER_ERROR_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NumberOfGuests that = (NumberOfGuests) object;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}
