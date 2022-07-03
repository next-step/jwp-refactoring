package kitchenpos.table.application;

import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.Exception.UnCompletedOrderStatusException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {
    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables()
                .getValue()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, getCannotUngroupTableGroupStatus())) {
            throw new UnCompletedOrderStatusException("단체 내 모든 테이블의 주문 상태가 주문 또는 식사 상태이면 단체 지정을 해제할 수 없습니다.");
        }
    }
}
