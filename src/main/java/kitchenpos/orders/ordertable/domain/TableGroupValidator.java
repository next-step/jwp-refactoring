package kitchenpos.orders.ordertable.domain;

import java.util.List;
import kitchenpos.common.domain.Validator;
import kitchenpos.orders.order.domain.OrderRepository;
import kitchenpos.orders.order.domain.OrderStatus;
import kitchenpos.common.DomainService;

@DomainService
class TableGroupValidator implements Validator<TableGroup> {

    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final TableGroup tableGroup) {
        final List<OrderTable> tables = tableGroup.getOrderTables();
        for (final OrderTable table : tables) {
            validateOrderTable(table);
        }
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndStatusNot(
            orderTable.getId(),
            OrderStatus.COMPLETION
        )) {
            throw new IllegalStateException("완료되지 않은 주문이 있는 주문 테이블을 단체 지정 제외할 수 없습니다.");
        }
    }
}
