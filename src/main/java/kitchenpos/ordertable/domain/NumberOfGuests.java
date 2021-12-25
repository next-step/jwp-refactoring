package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.ordertable.exception.TableChangeNumberOfGuestsException;

@Embeddable
public class NumberOfGuests {

    private final static String ERROR_MESSAGE_NUMBER_OF_GUESTS = "방문 손님 수는 0명 이상이어야 합니다.";

    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int inputNumberOfGuest) {
        if (inputNumberOfGuest < 0) {
            throw new TableChangeNumberOfGuestsException(ERROR_MESSAGE_NUMBER_OF_GUESTS);
        }
    }

    public int getNumberOfGuests() {
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
        return getNumberOfGuests() == that.getNumberOfGuests();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumberOfGuests());
    }
}
