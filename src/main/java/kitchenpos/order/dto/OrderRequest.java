package kitchenpos.order.dto;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public void validate(long menuCount) {
        validateParam();
        validateMenus(menuCount);
    }

    public void validateParam() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateMenus(long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException();
        }

    }

    public List<Long> getMenuIds() {
        if (orderLineItems != null) {
            return orderLineItems.stream()
                    .map(OrderLineItemRequest::getMenuId)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

}
