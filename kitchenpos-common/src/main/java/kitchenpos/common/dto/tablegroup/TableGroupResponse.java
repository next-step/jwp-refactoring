package kitchenpos.common.dto.tablegroup;

import java.time.LocalDateTime;
import java.util.Objects;

import kitchenpos.common.domain.tablegroup.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;

    protected TableGroupResponse() {}

    private TableGroupResponse(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TableGroupResponse that = (TableGroupResponse)o;
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate);
    }
}
