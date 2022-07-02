package kitchenpos.service.table.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableUpdateNumberOfGuestsRequest {
    private int numberOfGuests;

    public OrderTableUpdateNumberOfGuestsRequest() {
    }

    @JsonCreator
    public OrderTableUpdateNumberOfGuestsRequest(@JsonProperty("numberOfGuests") int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
