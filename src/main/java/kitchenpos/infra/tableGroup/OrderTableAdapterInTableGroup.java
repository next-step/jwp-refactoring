package kitchenpos.infra.tableGroup;

import kitchenpos.domain.order.exceptions.OrderEntityNotFoundException;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.tableGroup.SafeOrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableAdapterInTableGroup implements SafeOrderTable {
    private final OrderTableRepository orderTableRepository;

    public OrderTableAdapterInTableGroup(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderEntityNotFoundException("존재하지 않는 주문 테이블입니다."));
    }
}
