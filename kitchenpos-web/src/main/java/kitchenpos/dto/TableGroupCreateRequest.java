package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.model.TableGroup;

public class TableGroupCreateRequest {

    private List<Long> orderTableIds = Collections.emptyList();

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    protected TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public TableGroup toEntity() {
        return new TableGroup(LocalDateTime.now());
    }
}
