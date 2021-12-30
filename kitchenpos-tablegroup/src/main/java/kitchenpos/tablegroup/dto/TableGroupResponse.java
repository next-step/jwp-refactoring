package kitchenpos.tablegroup.dto;

import kitchenpos.tablegroup.domain.TableGroup;
import java.time.LocalDateTime;
import java.util.Objects;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;

    private TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate) {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(
            getCreatedDate(), that.getCreatedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreatedDate());
    }
}
