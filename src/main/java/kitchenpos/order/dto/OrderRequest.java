package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;

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

    public Orders toOrders() {
        return new Orders(null, this.orderTableId, OrderStatus.COOKING, LocalDateTime.now(), null);
    }
}
