package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTablesIds;

    public TableGroupResponse(Long id, LocalDateTime createdDate,
        List<Long> orderTablesId) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTablesIds = orderTablesId;
    }

    public static TableGroupResponse of(final TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
            tableGroup.getOrderTableIds());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTablesIds() {
        return orderTablesIds;
    }
}
