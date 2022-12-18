package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;

public interface ExistsOrderPort {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatus);
}
