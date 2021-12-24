package kitchenpos.tobe.orders.domain.ordertable;

import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.orders.domain.order.OrderRepository;
import kitchenpos.tobe.orders.domain.order.OrderStatus;

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
