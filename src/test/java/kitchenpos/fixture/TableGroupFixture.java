package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableRequest;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupFixture {

    public static TableGroupRequest 단체_지정_요청(List<OrderTable> orderTables) {
        return new TableGroupRequest(orderTables.stream()
                .map(orderTable -> new TableRequest(orderTable.getId()))
                .collect(Collectors.toList()));
    }
}
