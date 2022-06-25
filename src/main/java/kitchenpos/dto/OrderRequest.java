package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderStatusV2;
import kitchenpos.order.domain.OrdersV2;

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

    public OrdersV2 toOrders() {
        return new OrdersV2(null, this.orderTableId, OrderStatusV2.COOKING, LocalDateTime.now(), null);
    }
}
