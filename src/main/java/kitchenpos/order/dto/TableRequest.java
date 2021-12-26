package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableState;

import javax.validation.constraints.PositiveOrZero;

public class TableRequest {
    @PositiveOrZero
    private int numberOfGuests;
    private boolean empty;

    protected TableRequest() {
    }

    public TableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableRequest empty() {
        return new TableRequest(0, true);
    }

    public static TableRequest from(int numberOfGuests) {
        return new TableRequest(numberOfGuests, false);
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, new TableState(empty));
    }

    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
