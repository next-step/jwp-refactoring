package kitchenpos.dto.request;

import java.util.LinkedList;
import java.util.List;

public class TableGroupRequest {

    private Long id;
    private List<OrderTableRequest> orderTables = new LinkedList<>();

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
