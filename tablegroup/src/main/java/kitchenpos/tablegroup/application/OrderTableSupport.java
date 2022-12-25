package kitchenpos.tablegroup.application;

import java.util.List;

import kitchenpos.tablegroup.dto.OrderTableResponse;

public interface OrderTableSupport {
    List<OrderTableResponse> findOrderTables(List<Long> orderTableIds);
}
