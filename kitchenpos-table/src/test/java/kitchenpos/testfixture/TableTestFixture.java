package kitchenpos.testfixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;

import java.util.List;

public class TableTestFixture {
    public static OrderTableRequest createOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroupRequest createTableGroupRequest(List<Long> orderTablesIds) {
        return new TableGroupRequest(orderTablesIds);
    }
}
