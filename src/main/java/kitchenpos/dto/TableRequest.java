package kitchenpos.dto;

import javax.validation.constraints.Positive;

public class TableRequest {
    @Positive
    private int numberOfGuests;

    protected TableRequest(){}

    public TableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
