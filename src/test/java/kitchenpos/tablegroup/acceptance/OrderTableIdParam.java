package kitchenpos.tablegroup.acceptance;

import kitchenpos.table.acceptance.OrderTableId;

public class OrderTableIdParam {
    private final long id;

    public OrderTableIdParam(OrderTableId orderTableId) {
        this.id = orderTableId.value();
    }

    public long getId() {
        return id;
    }
}
