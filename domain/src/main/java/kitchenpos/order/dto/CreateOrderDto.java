package kitchenpos.order.dto;

import java.util.List;

public class CreateOrderDto {

    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItems;

    public CreateOrderDto() { }

    public CreateOrderDto(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
