package kitchenpos.table.dto;

import java.time.LocalDateTime;
import kitchenpos.table_group.domain.TableGroup;

public class TableGroupResponseDto {
    private Long id;
    private LocalDateTime createdDate;

    public TableGroupResponseDto(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroupResponseDto(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
