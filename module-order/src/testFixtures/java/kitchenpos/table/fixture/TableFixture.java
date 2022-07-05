package kitchenpos.table.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;

public class TableFixture {

    public static OrderTable 테이블_생성(int numberOfGuests, boolean empty) {
        return 테이블_생성(null, numberOfGuests, empty);
    }

    public static OrderTable 테이블_생성(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public static OrderTableRequest 테이블요청_생성(Long id, int numberOfGuests, boolean empty) {
        return new OrderTableRequest(id, numberOfGuests, empty);
    }

    public static TableGroup 테이블그룹_생성(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }

    public static TableGroupRequest 테이블그룹요청_생성() {
        List<OrderTableRequest> orderTableRequests = Arrays.asList(
                테이블요청_생성(1L, 2, true),
                테이블요청_생성(2L, 4, true)
        );
        return new TableGroupRequest(orderTableRequests);
    }

}
