package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {
    private Long id;
    private List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final Long id, final List<Long> orderTableIds) {
        this.id = id;
        this.orderTableIds = orderTableIds;
    }

    public Long getId() {
        return id;
    }

    public TableGroupRequest setId(final Long id) {
        this.id = id;
        return this;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroupRequest setOrderTableIds(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
        return this;
    }
}
