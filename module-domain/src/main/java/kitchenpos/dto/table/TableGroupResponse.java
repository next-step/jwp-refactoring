package kitchenpos.dto.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.TableGroup;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        List<OrderTableResponse> orderTables = mapToOrderTableResponse(tableGroup);
        return new TableGroupResponse(tableGroup.getId(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    private static List<OrderTableResponse> mapToOrderTableResponse(TableGroup tableGroup) {
        return tableGroup.getOrderTables()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }
}
