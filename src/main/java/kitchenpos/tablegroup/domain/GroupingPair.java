package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.Objects;

public class GroupingPair {
    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public GroupingPair(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupingPair that = (GroupingPair) o;
        return Objects.equals(tableGroupId, that.tableGroupId) && Objects.equals(orderTableIds, that.orderTableIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableGroupId, orderTableIds);
    }
}
