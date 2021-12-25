package kitchenpos.ordertable.exception;

public class IllegalNumberOfGuests extends RuntimeException {

    private final static String ERROR_MESSAGE_NUMBER_OF_GUESTS = "방문 손님 수는 0명 이상이어야 합니다.";

    public IllegalNumberOfGuests() {
        super(ERROR_MESSAGE_NUMBER_OF_GUESTS);
    }
}
