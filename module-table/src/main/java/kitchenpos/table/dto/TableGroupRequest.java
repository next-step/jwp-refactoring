package kitchenpos.table.dto;

import static kitchenpos.common.message.ValidationMessage.MIN_SIZE_IS_TWO;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Size;
import kitchenpos.table.domain.TableGroup;

public class TableGroupRequest {
    @Size(min = 2, message = MIN_SIZE_IS_TWO)
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
