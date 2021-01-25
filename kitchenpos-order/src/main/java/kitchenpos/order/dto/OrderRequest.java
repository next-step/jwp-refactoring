package kitchenpos.order.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class OrderRequest {
    private long orderTableId;
    @NotEmpty
    private List<OrderMenuRequest> orderMenuRequests;

    protected OrderRequest(){}

    public OrderRequest(long orderTableId, List<OrderMenuRequest> orderMenuRequests) {
        this.orderTableId = orderTableId;
        this.orderMenuRequests = orderMenuRequests;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderMenuRequest> getOrderMenuRequests() {
        return orderMenuRequests;
    }
}
