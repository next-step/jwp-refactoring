package kitchenpos.helper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;

public class TableGroupFixtures {
    public static TableGroupRequest 테이블_그룹_요청_만들기(List<OrderTableRequest> orderTableRequests) {
        return new TableGroupRequest(null, orderTableRequests);
    }

    public static TableGroup 테이블_그룹_만들기(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, LocalDateTime.now(), new OrderTables(orderTables));
    }

    public static TableGroup 테이블_그룹_만들기(Long id) {
        return 테이블_그룹_만들기(id, new ArrayList<>());
    }
}
