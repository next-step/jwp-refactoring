package kitchenpos.tablegroup.dto;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public void validateSavedSizeOfOrderTables(List<OrderTable> orderTables) {
        if (this.orderTables.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재 하지 않는 주문 테이블이 존재합니다.");
        }
    }

    public void validateRequestedSizeOfOrderTables() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 주문 테이블은 2개 이상 필요합니다.");
        }
    }
}
