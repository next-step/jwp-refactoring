package kitchenpos.order.dto;

import java.util.List;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderSaveRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class OrderSaveRequest {
    private Long orderTableId;
    private List<OrderLineItemSaveRequest> orderLineItems;

    public OrderSaveRequest() {
    }

    public OrderSaveRequest(Long orderTableId, List<OrderLineItemSaveRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemSaveRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
