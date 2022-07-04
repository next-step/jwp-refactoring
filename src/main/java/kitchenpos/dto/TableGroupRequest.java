package kitchenpos.dto;

import java.util.ArrayList;
import java.util.List;

public class TableGroupRequest {

    List<Long> orderTables = new ArrayList<>();

    public TableGroupRequest() {

    }

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
