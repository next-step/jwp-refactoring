package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroup toTableGroup(List<OrderTable> target) {
        TableGroup tableGroup = TableGroup.of();
        tableGroup.addOrderTable(target);
        return tableGroup;
    }

    public static TableGroupRequest of(List<Long> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        return new TableGroupRequest(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public void throwIfOrderTableSizeWrong(int savedOrderTableSize) {
        if (this.orderTableIds.size() != savedOrderTableSize) {
            throw new IllegalArgumentException();
        }
    }
}
