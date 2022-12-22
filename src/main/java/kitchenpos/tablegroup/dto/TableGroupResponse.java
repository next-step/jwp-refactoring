package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTableIds;

    protected TableGroupResponse() {}

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<Long> orderTableIds) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableIds);
    }

    public Long getId() {
        return id;
    }

}
