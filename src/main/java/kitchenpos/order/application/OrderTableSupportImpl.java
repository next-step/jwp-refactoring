package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.OrderTableSupport;
import kitchenpos.tablegroup.dto.OrderTableResponse;

@Component
public class OrderTableSupportImpl implements OrderTableSupport {
    private final OrderTableRepository orderTableRepository;

    public OrderTableSupportImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public List<OrderTableResponse> findOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        return orderTables.stream()
            .map(orderTable -> OrderTableResponse.of(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
            )).collect(Collectors.toList());
    }
}
