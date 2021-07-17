package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.TableGroup;

public class TableGroupRequest {

    private Long id;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(Long id) {
        this.id = id;
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest(Long id, List<OrderTableRequest> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroupRequest of(TableGroup tableGroup) {
        return new TableGroupRequest(tableGroup.getId(), OrderTableRequest.listOf(tableGroup.getOrderTables()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> findOrderTableIds() {
        return orderTables.stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toList());
    }

    public void setOrderTables(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup() {
        return new TableGroup(id, OrderTableRequest.toOrderTables(orderTables));
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
            "id=" + id +
            ", orderTables=" + orderTables +
            '}';
    }
}
