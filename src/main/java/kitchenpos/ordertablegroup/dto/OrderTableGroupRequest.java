package kitchenpos.ordertablegroup.dto;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;

import java.util.List;

public class OrderTableGroupRequest {
    private Long id;
    private List<Long> orderTableIds;

    public OrderTableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public OrderTableGroup toEntity(OrderTables orderTables) {
        return new OrderTableGroup(orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
