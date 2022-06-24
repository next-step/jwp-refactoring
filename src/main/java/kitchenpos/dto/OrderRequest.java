package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderRequest {
    private Long orderTableId;
    private List<Long> menuIds;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<Long> menuIds) {
        this.orderTableId = orderTableId;
        this.menuIds = menuIds;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public Order toOrder() {
        return new Order(null, this.orderTableId, OrderStatus.COOKING.name(), null, null);
    }
}
