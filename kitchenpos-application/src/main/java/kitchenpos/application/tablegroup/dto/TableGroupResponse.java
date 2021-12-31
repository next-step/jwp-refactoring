package kitchenpos.application.tablegroup.dto;

import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TableGroupResponse {

    private final LocalDateTime createdDate;
    private final List<Long> orderTableIds;
    private final Long id;

    public TableGroupResponse(Long id, List<Long> orderTableIds, LocalDateTime createdDate) {
        this.id = id;
        this.orderTableIds = orderTableIds;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getOrderTableIds(), tableGroup.getCreatedDate());
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;

        TableGroupResponse that = (TableGroupResponse) target;

        if (!Objects.equals(createdDate, that.createdDate)) return false;
        if (!Objects.equals(orderTableIds, that.orderTableIds))
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdDate, orderTableIds, id);
    }
}
