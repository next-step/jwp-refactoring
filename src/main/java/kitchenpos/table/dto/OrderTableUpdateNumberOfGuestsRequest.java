package kitchenpos.table.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableUpdateNumberOfGuestsRequest {
    private int numberOfGuests;

    public OrderTableUpdateNumberOfGuestsRequest() {
    }

    @JsonCreator
    public OrderTableUpdateNumberOfGuestsRequest(@JsonProperty("numberOfGuests") int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수가 적절하지 않습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
