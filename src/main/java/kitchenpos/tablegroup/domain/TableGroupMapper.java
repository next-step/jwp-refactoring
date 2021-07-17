package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.event.TableGroupUpdateEvent;

public class TableGroupMapper {
    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRequest tableGroupRequest;

    public TableGroupMapper(final TableGroupValidator tableGroupValidator, final TableGroupRequest tableGroupRequest) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRequest = tableGroupRequest;
        this.tableGroupValidator.validate(this.tableGroupRequest);
    }

    public static TableGroupMapper of(final TableGroupValidator tableGroupValidator, final TableGroupRequest tableGroupRequest) {
        return new TableGroupMapper(tableGroupValidator, tableGroupRequest);
    }

    public TableGroup toTableGroup() {
        return new TableGroup();
    }

    public TableGroupUpdateEvent toTableGroupUpdateEvent(final TableGroup tableGroup) {
        return new TableGroupUpdateEvent(tableGroup, tableGroupRequest.getOrderTableIdRequests());
    }
}
