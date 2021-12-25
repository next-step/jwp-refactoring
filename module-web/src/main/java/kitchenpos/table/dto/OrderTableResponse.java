package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Integer numberOfGuests;
    private String tableStatus;
    private Long orderId;

    public OrderTableResponse() {
    }

    private OrderTableResponse(Long id, Integer numberOfGuests, String tableStatus, Long orderId) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.tableStatus = tableStatus;
        this.orderId = orderId;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(),
            orderTable.getTableStatus(), orderTable.getOrderId());
    }

    public static List<OrderTableResponse> ofList(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public Long getOrderId() {
        return orderId;
    }
}
