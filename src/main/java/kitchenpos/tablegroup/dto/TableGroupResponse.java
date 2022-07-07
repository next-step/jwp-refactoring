package kitchenpos.tablegroup.dto;

import kitchenpos.tablegroup.domain.TableGroup;

import java.util.Objects;

public class TableGroupResponse {
    private Long id;

    protected TableGroupResponse() {
    }

    private TableGroupResponse(Long id) {
        this.id = id;
    }

    public static TableGroupResponse from(TableGroup savedTableGroup) {
        return new TableGroupResponse(savedTableGroup.getId());
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
