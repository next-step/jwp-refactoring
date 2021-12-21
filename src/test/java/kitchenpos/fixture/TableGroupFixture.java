package kitchenpos.fixture;

import kitchenpos.order.dto.OrderRequest;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupRequest;

import java.util.Arrays;
import java.util.List;

public class TableGroupFixture {

    public static TableGroup 생성() {
        return null;
    }

    public static TableGroupRequest 생성_Request(List<OrderRequest> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    public static TableGroupRequest 샘플_Request() {
        List<OrderRequest> orderTables = Arrays.asList(OrderFixture.샘플_Request_1L(), OrderFixture.샘플_Request_2L());
        return new TableGroupRequest(orderTables);
    }
}
