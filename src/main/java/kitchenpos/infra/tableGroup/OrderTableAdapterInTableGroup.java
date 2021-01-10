package kitchenpos.infra.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.orderTable.exceptions.OrderTableEntityNotFoundException;
import kitchenpos.domain.tableGroup.SafeOrderTableInTableGroup;
import org.springframework.stereotype.Component;

@Component
public class OrderTableAdapterInTableGroup implements SafeOrderTableInTableGroup {
    private final OrderTableRepository orderTableRepository;

    public OrderTableAdapterInTableGroup(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableEntityNotFoundException("존재하지 않는 주문 테이블입니다."));
    }
}
