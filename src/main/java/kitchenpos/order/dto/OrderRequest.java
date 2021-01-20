package kitchenpos.order.dto;


import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLIneItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLIneItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLIneItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

}
