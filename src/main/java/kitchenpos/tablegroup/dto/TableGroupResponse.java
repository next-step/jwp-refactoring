package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse from(TableGroup savedTableGroup) {
        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
