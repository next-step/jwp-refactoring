package kitchenpos.table.util;

import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

import java.util.ArrayList;
import java.util.List;

public class TableGroupResponseBuilder {
    private long id;
    private final List<OrderTableResponse> orderTables = new ArrayList<>();

    public TableGroupResponseBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public TableGroupResponseBuilder addOrderTable(Long id, int numberOfGuests, boolean empty) {
        orderTables.add(new OrderTableResponse(id, numberOfGuests, empty));
        return this;
    }

    public TableGroupResponse build() {
        return new TableGroupResponse(id, orderTables);
    }
}
