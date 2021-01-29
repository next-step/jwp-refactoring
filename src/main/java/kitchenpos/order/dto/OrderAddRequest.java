package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderAddRequest {

    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

    public void checkValidation() {
        if (this.orderLineItems.size() == 0) {
            throw new IllegalArgumentException();
        }
    }

    public void checkSameOrderLineSize(int size) {
        if (this.orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

}
