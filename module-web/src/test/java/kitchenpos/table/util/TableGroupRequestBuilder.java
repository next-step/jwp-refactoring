package kitchenpos.table.util;

import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupRequest;

import java.util.ArrayList;
import java.util.List;

public class TableGroupRequestBuilder {
    private final List<OrderTableIdRequest> orderTables = new ArrayList<>();

    public TableGroupRequestBuilder addOrderTable(long id) {
        orderTables.add(new OrderTableIdRequest(id));
        return this;
    }

    public TableGroupRequest build() {
        return new TableGroupRequest(orderTables);
    }
}
