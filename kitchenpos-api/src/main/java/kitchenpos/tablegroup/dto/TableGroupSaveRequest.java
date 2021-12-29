package kitchenpos.tablegroup.dto;

import kitchenpos.table.dto.OrderTableRequest;

import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * packageName : kitchenpos.dto
 * fileName : TableGroupSaveRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class TableGroupSaveRequest {
    private List<OrderTableRequest> orderTables;

    private TableGroupSaveRequest() {
    }

    private TableGroupSaveRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupSaveRequest of(List<OrderTableRequest> orderTables) {
        return new TableGroupSaveRequest(orderTables);
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(toList()
                );
    }

}
