package kitchenpos.table.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;

public class TableGroupFixtureFactory {
    private TableGroupFixtureFactory() {
    }

    public static TableGroupRequest createTableGroup(List<OrderTableResponse> orderTables) {
        return new TableGroupRequest(orderTables.stream().map(OrderTableResponse::getId).collect(Collectors.toList()));
    }
}
