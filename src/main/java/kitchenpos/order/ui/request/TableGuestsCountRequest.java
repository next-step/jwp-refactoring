package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.order.domain.Headcount;

public final class TableGuestsCountRequest {

    private final int numberOfGuests;

    @JsonCreator
    public TableGuestsCountRequest(@JsonProperty("numberOfGuests") int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Headcount numberOfGuests() {
        return Headcount.from(numberOfGuests);
    }
}
