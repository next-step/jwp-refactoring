package kitchenpos.order.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderRequest {
    private static final String EXCEPTION_MESSAGE_INVALID_MENU_PRODUCTS = "유효한 Order Line Item 이 없습니다! 값을 확인해주세요.";
    private Long orderTableId;
    private String orderStatus;
    private List<OrderMenuRequest> orderLineItems;

    private OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderMenuRequest> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderMenuRequest> getOrderLineItems() {
        return orderLineItems;
    }

    private void validateOrderLineItems(List<OrderMenuRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_INVALID_MENU_PRODUCTS);
        }
    }
}
