package kitchenpos.table.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;

public class OrderTableRequest {

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

    public Headcount numberOfGuests() {
        return Headcount.from(numberOfGuests);
    }

    public TableStatus status() {
        return TableStatus.valueOf(empty);
    }

    public OrderTable toEntity() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(Headcount.from(numberOfGuests));
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
