package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.OrderTableResponse;
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

    public static OrderTableResponse savedOrderTableResponse(Long id, Long tableGroupId, boolean empty) {
        return OrderTableResponse.of(id, tableGroupId, 0, empty);
    }
}
