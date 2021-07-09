package kitchenpos.table.domain;

import kitchenpos.common.exception.InvalidNumberOfGeustsException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGeusts {

    private static final String EXCEPTION_MESSAGE_MIN_NUMBER_OF_GUESTS = "손님의 수는 %s보다 작을수 없습니다.";
    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    public NumberOfGeusts() {
    }

    private NumberOfGeusts(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGeusts of(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new InvalidNumberOfGeustsException(String.format(EXCEPTION_MESSAGE_MIN_NUMBER_OF_GUESTS, MIN_NUMBER_OF_GUESTS));
        }

        return new NumberOfGeusts(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

}
