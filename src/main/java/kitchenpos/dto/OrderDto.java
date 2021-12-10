package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    protected OrderDto() {
    }

    private OrderDto(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderDto of(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemDto> orderLineItems) {
        return new OrderDto(id, orderTableId, orderStatus, orderedTime, orderLineItems);  
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return this.orderedTime;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return this.orderLineItems;
    }
}
