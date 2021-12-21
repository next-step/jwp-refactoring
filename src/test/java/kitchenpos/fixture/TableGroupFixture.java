package kitchenpos.fixture;

import kitchenpos.tableGroup.dto.OrderTableIdRequest;
import kitchenpos.tableGroup.dto.TableGroupRequest;

import java.util.Arrays;
import java.util.List;

public class TableGroupFixture {

    public static TableGroupRequest 생성_Request(List<OrderTableIdRequest> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    public static TableGroupRequest 샘플_Request() {
        OrderTableIdRequest 샘플1 = new OrderTableIdRequest(1L);
        OrderTableIdRequest 샘플2 = new OrderTableIdRequest(2L);

        return new TableGroupRequest(Arrays.asList(샘플1, 샘플2));
    }
}
