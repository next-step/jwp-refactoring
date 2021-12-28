package kitchenpos.table.domain;

import java.util.Collection;

public interface OrderStatusChecker {
    boolean existsNotCompletedOrderByOrderTableIds(Collection<Long> tableIds);
}
