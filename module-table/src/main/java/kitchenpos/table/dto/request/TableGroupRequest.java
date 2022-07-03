package kitchenpos.table.dto.request;

import java.util.List;

public class TableGroupRequest {

    private Long id;
    private List<Long> orderTableIds;

    public TableGroupRequest(Long id, List<Long> orderTableIds) {
        this.id = id;
        this.orderTableIds = orderTableIds;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
