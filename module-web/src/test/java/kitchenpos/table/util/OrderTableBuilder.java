package kitchenpos.table.util;

import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

public class OrderTableBuilder {
    private long id;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public OrderTableBuilder withNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public OrderTableBuilder withEmpty(boolean empty) {
        this.empty = empty;
        return this;
    }

    public OrderTableRequest requestBuild() {
        return new OrderTableRequest(id, numberOfGuests, empty);
    }

    public OrderTableResponse responseBuilder() {
        return new OrderTableResponse(id, numberOfGuests, empty);
    }



}
