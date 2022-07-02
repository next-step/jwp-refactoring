package kitchenpos.dto;

import java.util.ArrayList;
import java.util.List;

public class TableGroupRequest {
    private Long id;
    private List<OrderTableRequest> orderTables = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public int getOrderTableSize() {
        return orderTables == null ? 0 : orderTables.size();
    }

    public void setOrderTables(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }
}
