package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.tablegroup.dto.TableGroupCreateEvent;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupUngroupEvent;

public class TableGroupFixture {
    private TableGroupFixture() {
    }

    public static TableGroupRequest tableGroupRequest(List<Long> orderTableId) {
        return new TableGroupRequest(orderTableId);
    }

    public static TableGroup savedTableGroup(Long id) {
        return new TableGroup(id, LocalDateTime.now());
    }

    public static TableGroupCreateEvent tableGroupCreateEvent(List<Long> orderTableIds, Long tableGroupId) {
        return new TableGroupCreateEvent(orderTableIds, tableGroupId);
    }

    public static TableGroupUngroupEvent tableGroupUngroupEvent(Long tableGroupId) {
        return new TableGroupUngroupEvent(tableGroupId);
    }
}
