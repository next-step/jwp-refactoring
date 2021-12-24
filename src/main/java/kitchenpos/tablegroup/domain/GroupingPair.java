package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.Objects;

public class GroupingPair {
    private final TableGroup tableGroup;
    private final List<Long> orderTableIds;

    public GroupingPair(TableGroup tableGroup, List<Long> orderTableIds) {
        this.tableGroup = tableGroup;
        this.orderTableIds = orderTableIds;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupingPair that = (GroupingPair) o;
        return Objects.equals(tableGroup, that.tableGroup) && Objects.equals(orderTableIds, that.orderTableIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableGroup, orderTableIds);
    }
}
