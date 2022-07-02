package kitchenpos.service.tablegroup.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.service.table.dto.OrderTableRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    @JsonCreator
    public TableGroupRequest(@JsonProperty("orderTables") List<OrderTableRequest> orderTables) {
        check(orderTables);
        this.orderTables = orderTables;
    }

    private void check(List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블 갯수가 적절하지 않습니다.");
        }
    }

    public List<OrderTableRequest> getOrderTables() {
        return this.orderTables;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.stream().map(OrderTableRequest::getId).collect(Collectors.toList());

    }
}
