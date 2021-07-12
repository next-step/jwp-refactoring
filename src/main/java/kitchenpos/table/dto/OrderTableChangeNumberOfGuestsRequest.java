package kitchenpos.table.dto;

import org.hibernate.validator.constraints.Range;

public class OrderTableChangeNumberOfGuestsRequest {

    @Range
    private Integer numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest() {
        // empty
    }

    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
