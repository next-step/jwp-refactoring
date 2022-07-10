package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;

import java.util.List;

public class TableServiceFactory {
    public static OrderTableRequest 테이블요청생성(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static TableGroup 테이블그룹생성(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(orderTables);
        tableGroup.setId(id);
        return tableGroup;
    }
    public static TableGroupRequest 테이블그룹요청생성(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }
}
