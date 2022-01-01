package kitchenpos.ordertable.domain;

import kitchenpos.domain.DomainService;
import kitchenpos.domain.Validator;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;

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
