package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;
    private LocalDateTime createDate;

    public TableGroupResponse() {

    }

    public TableGroupResponse(TableGroup saveTableGroup) {
        id = saveTableGroup.getId();
        orderTables = generateOrderTableResponse(saveTableGroup.getOrderTables());
    }

    private List<OrderTableResponse> generateOrderTableResponse(List<OrderTable> orderTables) {
        return orderTables
                .stream()
                .map(OrderTableResponse::formOrderTable)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
