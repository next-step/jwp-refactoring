package kitchenpos.dto.request;

import kitchenpos.domain.TableGroupCreate;

import java.util.List;

public class TableGroupCreateRequest {
    private List<Long> orderTableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
