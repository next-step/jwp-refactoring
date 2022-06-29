package kitchenpos.table.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds = new ArrayList<>();

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds.addAll(orderTableIds);
    }

    public List<Long> getOrderTableIds() {
        return Collections.unmodifiableList(orderTableIds);
    }
}
