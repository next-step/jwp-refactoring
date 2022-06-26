package kitchenpos.dto.request;

import java.util.LinkedList;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.dto.OrderLineItemDTO;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemDTO> orderLineItems = new LinkedList<>();
    private OrderStatus orderStatus;

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public List<OrderLineItemDTO> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItemDTO> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
