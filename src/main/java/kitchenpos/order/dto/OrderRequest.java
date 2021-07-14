package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    /*public List<Long> getMenuIds() {
        if(CollectionUtils.isEmpty(orderLineItemRequests)){
            throw new EmptyOrderLineItemsException();
        }
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public boolean isEqualsMenuSize(long countByIdIn) {
        return orderLineItemRequests.size() == countByIdIn;
    }*/

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
