package kitchenpos.domain.table_group.fixture;

import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table_group.dto.TableGroupRequest;
import kitchenpos.domain.table.dto.TableRequest;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupFixture {

    public static TableGroupRequest 단체_지정_요청(List<OrderTable> orderTables) {
        return new TableGroupRequest(orderTables.stream()
                .map(orderTable -> new TableRequest(orderTable.getId()))
                .collect(Collectors.toList()));
    }
}
