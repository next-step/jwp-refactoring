package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus.name();
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    @JsonIgnore
    public boolean isEmptyOrderLineItems() {
        return CollectionUtils.isEmpty(orderLineItems);
    }

    @JsonIgnore
    public int getOrderLineItemsSize() {
        return orderLineItems.size();
    }

    @JsonIgnore
    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
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

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
