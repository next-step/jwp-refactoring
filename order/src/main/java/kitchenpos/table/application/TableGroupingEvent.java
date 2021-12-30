package kitchenpos.table.application;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class TableGroupingEvent extends ApplicationEvent {

    private Long tableGroupId;
    private List<Long> orderTableIds;

    public TableGroupingEvent(Object source, Long tableGroupId, List<Long> orderTableIds) {
        super(source);
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
