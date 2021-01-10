package kitchenpos.domain.domainEvents;

import java.util.List;

public class GroupOrderTableEvent {
    private final List<Long> orderTableIds;

    public GroupOrderTableEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
