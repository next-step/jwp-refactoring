package kitchenpos.dto.response;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.dto.dto.OrderLineItemDTO;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemDTO> orderLineItems = new LinkedList<>();

    protected OrderResponse(Long id, Long orderTableId, String orderStatus,
        List<OrderLineItemDTO> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        List<OrderLineItemDTO> orderLineItemDTOs = order.getOrderLineItems()
            .stream()
            .map(OrderLineItemDTO::of)
            .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTableId(),
            order.getOrderStatus().name(),
            orderLineItemDTOs);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemDTO> getOrderLineItems() {
        return orderLineItems;
    }
}
