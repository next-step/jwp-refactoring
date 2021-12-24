package kitchenpos.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.domain.tablegroup.TableGroup;

@Embeddable
public final class TableGroupId {
    @Column(name = "table_group_id")
    private final Long tableGroupId;

    protected TableGroupId() {
        this.tableGroupId = null;
    }

    private TableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public static TableGroupId of(Long tableGroupId) {
        return new TableGroupId(tableGroupId);
    }
    public static TableGroupId of(TableGroup tableGroup) {
        return new TableGroupId(tableGroup.getId());
    }

    public Long value() {
        return this.tableGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TableGroupId)) {
            return false;
        }
        TableGroupId tableGroupId = (TableGroupId) o;
        return Objects.equals(this.tableGroupId, tableGroupId.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tableGroupId);
    }
}