package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;

import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {
    private final Long id;

    private final LocalDateTime createdDate;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse of(final TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
