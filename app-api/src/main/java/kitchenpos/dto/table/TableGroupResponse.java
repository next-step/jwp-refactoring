package kitchenpos.dto.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableGroup;

import java.util.List;

public class TableGroupResponse {
    private Long id;
    private List<TableResponse> tableResponses;

    public TableGroupResponse(Long id, List<TableResponse> tableResponses) {
        this.id = id;
        this.tableResponses = tableResponses;
    }

    public static TableGroupResponse of(OrderTableGroup orderTableGroup, List<OrderTable> orderTables) {
        return new TableGroupResponse(orderTableGroup.getId(), TableResponse.ofList(orderTables));
    }

    public Long getId() {
        return id;
    }

    public List<TableResponse> getTableResponses() {
        return tableResponses;
    }
}
