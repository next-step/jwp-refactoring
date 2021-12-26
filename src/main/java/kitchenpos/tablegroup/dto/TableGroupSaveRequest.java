package kitchenpos.tablegroup.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Long> getTableIds() {
        return orderTables.stream().map(OrderTableRequest::getId).collect(toList());
    }
}
