package kitchenpos.dto;

import javax.validation.constraints.Min;

public class TableChangeNumberOfGuestRequest {

    @Min(value = 0)
    private int numberOfGuests;

    public TableChangeNumberOfGuestRequest() {
    }

    public TableChangeNumberOfGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
