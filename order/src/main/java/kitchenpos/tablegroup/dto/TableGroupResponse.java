package kitchenpos.tablegroup.dto;

import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

    private Long id;

    private TableGroupResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup);
    }

    public Long getId() {
        return id;
    }
}
