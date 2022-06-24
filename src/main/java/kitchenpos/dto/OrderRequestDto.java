package kitchenpos.dto;

import java.util.List;

public class OrderRequestDto {
    private Long orderTableId;
    private List<OrderLineItemRequestDto> orderLineItems;

    public OrderRequestDto() {
    }

    public OrderRequestDto(Long orderTableId, List<OrderLineItemRequestDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequestDto> getOrderLineItems() {
        return orderLineItems;
    }
}
