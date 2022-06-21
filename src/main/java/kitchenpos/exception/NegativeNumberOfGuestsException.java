package kitchenpos.exception;

public class NegativeNumberOfGuestsException extends RuntimeException {
    public static final String INVALID_NUMBER_OF_GUESTS = "손님(NumberOfGuest)은 0보다 작은을 수 없습니다. (input = %s)";

    public NegativeNumberOfGuestsException(int numberOfGuests) {
        super(String.format(INVALID_NUMBER_OF_GUESTS, numberOfGuests));
    }
}
