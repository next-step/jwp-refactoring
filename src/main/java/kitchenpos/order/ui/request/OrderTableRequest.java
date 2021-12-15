package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;

public final class OrderTableRequest {

    private final int numberOfGuests;
    private final boolean empty;

    @JsonCreator
    public OrderTableRequest(
        @JsonProperty("numberOfGuests") int numberOfGuests,
        @JsonProperty("empty") boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return OrderTable.of(Headcount.from(numberOfGuests), TableStatus.valueOf(empty));
    }
}
