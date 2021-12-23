package kitchenpos.order.dto;

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
    private List<Long> orderTableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public TableGroup toEntity(List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
