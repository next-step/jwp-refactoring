package kitchenpos.table.domain.request;

import java.util.List;

public class TableGroupRequest {

    private Long id;
    private List<OrderTableRequest> orderTableRequest;

    public TableGroupRequest(Long id, List<OrderTableRequest> orderTableRequest) {
        this.id = id;
        this.orderTableRequest = orderTableRequest;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableRequest> getOrderTableRequest() {
        return orderTableRequest;
    }
}
