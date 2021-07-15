package kitchenpos.ordertable.dto;

import java.util.List;

public class TableGroupRequest {
    private Long id;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest(Long id, List<OrderTableRequest> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }
}
