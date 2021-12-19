package kitchenpos.table.dto;

public class ChangeTableGuestRequest {
    private int numberOfGuests;

    public ChangeTableGuestRequest() {
    }

    private ChangeTableGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static ChangeTableGuestRequest of(int numberOfGuests) {
        return new ChangeTableGuestRequest(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
