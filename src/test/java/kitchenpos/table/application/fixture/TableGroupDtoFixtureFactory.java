package kitchenpos.table.application.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;

public class TableGroupDtoFixtureFactory {
    private TableGroupDtoFixtureFactory() {
    }

    public static TableGroupRequest createTableGroup(List<OrderTableResponse> orderTables) {
        return new TableGroupRequest(orderTables.stream().map(OrderTableResponse::getId).collect(Collectors.toList()));
    }
}
