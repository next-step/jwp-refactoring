package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableState;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

public class OrderTableRequest {
    private Long id;
    @PositiveOrZero
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest() {
    }

    public OrderTableRequest(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests, false);
    }

    public static OrderTableRequest empty() {
        return new OrderTableRequest(0, true);
    }

    public static OrderTableRequest group(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests, false);
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, new TableState(empty));
    }

    public Long getId() {
        return this.id;
    }

    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
