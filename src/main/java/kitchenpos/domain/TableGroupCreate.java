package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableGroupCreate {
    private List<Long> orderTableIds;

    public TableGroupCreate(List<Long> orderTableIds) {
        validate(orderTableIds);
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    private void validate(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public boolean orderTableHasSize(int size) {
        return orderTableIds.size() == size;
    }
}
