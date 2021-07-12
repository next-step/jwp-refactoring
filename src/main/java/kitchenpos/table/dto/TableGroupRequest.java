package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.exception.NotExistOrderTable;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTableRequests;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public TableGroup toTableGroup(List<OrderTable> orderTables){
        if (orderTables.size() != orderTableRequests.size()) {
            throw new NotExistOrderTable();
        }
        return new TableGroup(new OrderTables(orderTables));
    }

    public List<Long> getOrderTableIds() {
        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이어야 합니다.");
        }
        return orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}
