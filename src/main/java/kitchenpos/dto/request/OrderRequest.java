package kitchenpos.dto.request;

import kitchenpos.advice.exception.OrderException;
import kitchenpos.advice.exception.OrderLineItemException;
import kitchenpos.domain.OrderStatus;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public void validateEmptyOrderLineItems() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderLineItemException("orderLineItems가 존재하지 않습니다");
        }
    }

    public void validateMenuSize(long size) {
        if (getMenuIds().size() != size) {
            throw new OrderException("주문 메뉴의 사이즈가 다릅니다", size);
        }
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }


    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
