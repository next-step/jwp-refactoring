package kitchenpos.table.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.domain.Headcount;

public class TableGuestsCountRequest {

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
