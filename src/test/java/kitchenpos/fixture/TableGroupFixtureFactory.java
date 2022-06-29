package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupRequest;

public class TableGroupFixtureFactory {
    private TableGroupFixtureFactory() {
    }

    public static TableGroupRequest createTableGroup(List<OrderTableResponse> orderTables) {
        return new TableGroupRequest(orderTables.stream().map(OrderTableResponse::getId).collect(Collectors.toList()));
    }
}
