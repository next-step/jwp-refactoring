package kitchenpos.ordertablegroup.dto;

import java.util.List;

public class OrderTableGroupRequest {
    private Long id;
    private List<Long> orderTableIds;

    protected OrderTableGroupRequest() {
    }

    public OrderTableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
