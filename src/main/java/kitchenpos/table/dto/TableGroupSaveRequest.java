package kitchenpos.table.dto;

import java.util.List;

/**
 * packageName : kitchenpos.dto
 * fileName : TableGroupSaveRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class TableGroupSaveRequest {
    private List<OrderTableRequest> orderTables;

    public TableGroupSaveRequest() {
    }

    public TableGroupSaveRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
