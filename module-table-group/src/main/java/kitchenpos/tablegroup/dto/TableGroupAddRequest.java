package kitchenpos.tablegroup.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableGroupAddRequest {

    private List<OrderTableDto> orderTables;

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public void checkSameSize(int size) {
        if (orderTables.size() != size) {
            throw new IllegalArgumentException();
        }
    }
}
