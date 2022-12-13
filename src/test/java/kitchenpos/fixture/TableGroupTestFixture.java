package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;

import java.util.Arrays;
import java.util.List;

public class TableGroupTestFixture {

    public static TableGroupRequest 테이블그룹요청(List<OrderTableRequest> orderTables) {
        return TableGroupRequest.of(null, null, orderTables);
    }

    public static TableGroup 테이블그룹() {
        return TableGroup.of(Arrays.asList(
                OrderTable.of(null, 10, true),
                OrderTable.of(null, 10, true)
        ));
    }
}
