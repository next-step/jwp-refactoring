package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {

    private LocalDateTime createdDate;
    private List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(LocalDateTime createdDate, List<Long> orderTableIds) {
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public void setOrderTableIds(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }
}
