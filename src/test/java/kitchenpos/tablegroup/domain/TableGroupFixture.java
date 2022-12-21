package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.tablegroup.dto.TableGroupCreatedEvent;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupUngroupedEvent;

public class TableGroupFixture {
    private TableGroupFixture() {
    }

    public static TableGroupRequest tableGroupRequest(List<Long> orderTableId) {
        return new TableGroupRequest(orderTableId);
    }

    public static TableGroup savedTableGroup(Long id) {
        return new TableGroup(id, LocalDateTime.now());
    }

    public static TableGroupCreatedEvent tableGroupCreateEvent(List<Long> orderTableIds, Long tableGroupId) {
        return new TableGroupCreatedEvent(orderTableIds, tableGroupId);
    }

    public static TableGroupUngroupedEvent tableGroupUngroupEvent(Long tableGroupId) {
        return new TableGroupUngroupedEvent(tableGroupId);
    }
}
