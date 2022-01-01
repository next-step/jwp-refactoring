package kitchenpos.table.dto;

import kitchenpos.moduledomain.table.NumberOfGuests;
import kitchenpos.moduledomain.table.OrderTable;
import kitchenpos.moduledomain.table.OrderTableStatus;

public class OrderTableRequest {

    private Long id;
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    public OrderTableRequest(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest() {
    }

    public OrderTable toOrderTable() {
        return OrderTable.of(numberOfGuests, OrderTableStatus.valueOf(empty));
    }

    public Long getId() {
        return id;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}
