package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupTestFixture {

    public static TableGroupRequest createTableGroup(Long id, LocalDateTime createTime, List<OrderTable> orderTables) {
        return TableGroupRequest.of(id, createTime, orderTables);
    }

    public static TableGroupRequest createTableGroup(List<OrderTable> orderTables) {
        return TableGroupRequest.of(null, null, orderTables);
    }
}
