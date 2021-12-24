package kitchenpos.order.dto;

import kitchenpos.order.application.exception.OrderTableNotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {
    @Unique
    @Size(min = 2)
    @NotEmpty
    private List<Long> tableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public TableGroup toEntity(List<OrderTable> orderTables) {
        if (tableIds.size() != orderTables.size()) {
            throw new OrderTableNotFoundException();
        }
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
