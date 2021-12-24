package kitchenpos.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.domain.tablegroup.TableGroup;

@Embeddable
public final class TableGroupId {
    @Column(name = "table_group_id")
    private final Long id;

    protected TableGroupId() {
        this.id = null;
    }

    private TableGroupId(Long id) {
        this.id = id;
    }

    public static TableGroupId of(Long id) {
        return new TableGroupId(id);
    }
    public static TableGroupId of(TableGroup tableGroup) {
        return new TableGroupId(tableGroup.getId());
    }

    public Long value() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TableGroupId)) {
            return false;
        }
        TableGroupId id = (TableGroupId) o;
        return Objects.equals(this.id, id.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}