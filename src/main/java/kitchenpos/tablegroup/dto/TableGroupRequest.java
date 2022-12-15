package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {

    private List<Long> orderTableRequests;

    public TableGroupRequest() {
    }

    private TableGroupRequest(List<Long> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public static TableGroupRequest of(List<Long> orderTableRequests){
        return new TableGroupRequest(orderTableRequests);
    }

    public List<Long> getOrderTableRequests() {
        return orderTableRequests;
    }


}
