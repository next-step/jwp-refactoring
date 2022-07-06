package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {
    private final Long id;

    private TableGroupResponse(Long id) {
        this.id = id;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId());
    }

    public Long getId() {
        return id;
    }
}
