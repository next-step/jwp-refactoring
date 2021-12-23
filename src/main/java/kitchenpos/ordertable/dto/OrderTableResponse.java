package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableResponse {

    private Long id;

    private int numberOfGuests;

    private boolean orderClose;

    private OrderTableResponse() {
    }

    private OrderTableResponse(Long id, int numberOfGuests,
        boolean orderClose) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.orderClose = orderClose;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isOrderClose() {
        return orderClose;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(),
            orderTable.isOrderClose());
    }
}
