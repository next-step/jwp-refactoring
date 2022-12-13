package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.util.List;

public class TableGroupResponse {
    private Long id;
    private List<Long> orderTableIds;

    public TableGroupResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.orderTableIds = tableGroup.getOrderTableIds();
    }

    public static TableGroupResponse of(TableGroup tableGroup){
        return new TableGroupResponse(tableGroup);
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
