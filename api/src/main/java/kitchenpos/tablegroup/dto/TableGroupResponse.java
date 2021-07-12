package kitchenpos.tablegroup.dto;

import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {
    private Long id;

    public TableGroupResponse() {}

    public TableGroupResponse(Long id) {
        this.id = id;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId());
    }

    public Long getId() {
        return id;
    }
}
