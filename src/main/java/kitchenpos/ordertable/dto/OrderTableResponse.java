package kitchenpos.ordertable.dto;

import java.util.List;
import java.util.stream.Collectors;
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

    public static List<OrderTableResponse> fromList(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(),
            orderTable.isOrderClose());
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
}
