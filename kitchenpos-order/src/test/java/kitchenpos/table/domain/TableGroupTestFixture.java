package kitchenpos.table.domain;

import java.util.List;
import kitchenpos.table.dto.TableGroupRequest;

public class TableGroupTestFixture {
    public static TableGroupRequest tableGroupRequest(List<Long> ids) {
        return new TableGroupRequest(ids);
    }

    public static TableGroupRequest tableGroupRequest(Long... ids) {
        return new TableGroupRequest(ids);
    }

    public static TableGroup tableGroup(Long id, List<OrderTable> orderTables) {
        return TableGroup.of(id, orderTables);
    }
}
