package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;

public class TableGroupFixture {
    private TableGroupFixture() {
    }

    public static TableGroupRequest tableGroupRequest(List<Long> orderTableId) {
        return new TableGroupRequest(orderTableId);
    }

    public static TableGroup savedTableGroup(Long id) {
        return new TableGroup(id, LocalDateTime.now());
    }
}
