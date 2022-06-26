package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;

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
        final OrderTable orderTable = new OrderTable(null, null, 5, false);
        return new Orders(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), null);
    }
}
