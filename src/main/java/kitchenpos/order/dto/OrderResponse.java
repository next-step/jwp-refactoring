package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableResponse;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems = new ArrayList<>();

    public OrderResponse() {
    }

    public OrderResponse(Long id, OrderTableResponse orderTable, String orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order created) {
        OrderTableResponse orderTableResponse = new OrderTableResponse(created.getOrderTableId());
        List<OrderLineItemResponse> orderLineItems = createOrderLineItemResponses(created.getOrderLineItems());
        return new OrderResponse(created.getId(), orderTableResponse, created.getOrderStatus(),
                created.getOrderedTime(), orderLineItems);
    }

    public static OrderResponse of(Order created, OrderTable orderTable) {
        OrderTableResponse orderTableResponse = createOrderTableResponse(orderTable);
        List<OrderLineItemResponse> orderLineItems = createOrderLineItemResponses(created.getOrderLineItems());
        return new OrderResponse(created.getId(), orderTableResponse, created.getOrderStatus(),
                created.getOrderedTime(), orderLineItems);
    }

    private static List<OrderLineItemResponse> createOrderLineItemResponses(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(),
                        orderLineItem.getQuantity(), orderLineItem.getOrderLineItemMenuName(),
                        orderLineItem.getOrderLineItemMenuPrice()))
                .collect(Collectors.toList());
    }

    private static OrderTableResponse createOrderTableResponse(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTableResponse orderTable) {
        this.orderTable = orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItemResponse> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
