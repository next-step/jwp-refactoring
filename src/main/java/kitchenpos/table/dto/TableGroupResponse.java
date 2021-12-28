package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
