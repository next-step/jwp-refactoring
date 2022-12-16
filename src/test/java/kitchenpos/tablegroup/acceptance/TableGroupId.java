package kitchenpos.tablegroup.acceptance;

import java.util.Objects;

public class TableGroupId {
    private final long tableGroupId;

    public TableGroupId(long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public long value() {
        return tableGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TableGroupId that = (TableGroupId)o;
        return tableGroupId == that.tableGroupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableGroupId);
    }
}
