package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.TableGroupV2;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroupV2 toTableGroup() {
        return new TableGroupV2(LocalDateTime.now());
    }
}
