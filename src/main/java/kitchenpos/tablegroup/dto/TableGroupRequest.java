package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.dto.OrderTableRequest;
import org.springframework.util.CollectionUtils;

public class TableGroupRequest {
    private Long id;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(Long id, List<OrderTableRequest> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest of(TableGroup tableGroup) {
        List<OrderTableRequest> orderTableRequests = tableGroup.getOrderTables().stream()
                .map(OrderTableRequest::of)
                .collect(Collectors.toList());

        return new TableGroupRequest(tableGroup.getId(), orderTableRequests);
    }

    public void validateOrderTableSize() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new OrderTableException("주문 테이블이 비어있거나 2개미만입니다");
        }
    }


    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
