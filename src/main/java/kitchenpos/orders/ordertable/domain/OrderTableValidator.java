package kitchenpos.orders.ordertable.domain;

import kitchenpos.common.DomainService;
import kitchenpos.common.domain.Validator;
import kitchenpos.orders.order.domain.OrderRepository;
import kitchenpos.orders.order.domain.OrderStatus;

@DomainService
class OrderTableValidator implements Validator<OrderTable> {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndStatusNot(
            orderTable.getId(),
            OrderStatus.COMPLETION
        )) {
            throw new IllegalStateException("완료되지 않은 주문이 있는 주문 테이블은 빈 테이블로 설정할 수 없습니다.");
        }
    }
}
