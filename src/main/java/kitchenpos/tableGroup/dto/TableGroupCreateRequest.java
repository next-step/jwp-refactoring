package kitchenpos.tableGroup.dto;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;

import java.util.ArrayList;
import java.util.List;

public class TableGroupCreateRequest {
    private List<Long> orderTables = new ArrayList<>();

    protected TableGroupCreateRequest() {}

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTables.addAll(orderTableIds);
    }

    public TableGroup of(OrderTables orderTables) {
        checkSavedTableGroupCount(orderTables);

        return new TableGroup(orderTables);
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }

    private void checkSavedTableGroupCount(OrderTables orderTables) {
        if (orderTables.getValue().size() != this.orderTables.size()) {
            throw new IllegalArgumentException("생성 요청된 단체 지정에 존재하지 않는 주문 테이블이 있습니다.");
        }
    }
}
