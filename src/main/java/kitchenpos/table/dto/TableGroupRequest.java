package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Size;
import kitchenpos.table.domain.TableGroup;

public class TableGroupRequest {
    @Size(min = 2)
    private List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup toTableGroup() {
        return new TableGroup(LocalDateTime.now());
    }
}
