package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;

public class OrderTableRequest {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity(){
        return new OrderTable(this.getNumberOfGuests(), this.empty);
    }
}
