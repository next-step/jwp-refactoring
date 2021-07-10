package kitchenpos.domain.table;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableGroupCreate {
    private static final int MINIMUM = 2;

    private List<Long> orderTableIds;

    public TableGroupCreate(List<Long> orderTableIds) {
        validate(orderTableIds);
        this.orderTableIds = orderTableIds;
    }

    private void validate(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MINIMUM) {
            throw new IllegalArgumentException();
        }
    }

    public int sizeOfOrderTableIds() {
        return orderTableIds.size();
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
