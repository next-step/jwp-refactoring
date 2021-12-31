package kitchenpos.order.domain.validator;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.validator.OrderTableChangeEmptyValidator;
import kitchenpos.ordertable.exception.CanNotChangeOrderTableException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderOrderTableChangeEmptyValidator implements OrderTableChangeEmptyValidator {
    private static final String CHANGE_EMPTY_ORDER_ERROR_MESSAGE = "주문 테이블의 주문 상태가 조리나 식사일 경우가 아닐 경우에만 테이블의 빈 유무를 변경할 수 있습니다.";
    private final OrderRepository orderRepository;

    public OrderOrderTableChangeEmptyValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void validate(Long orderTableId) {
        if (!orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getCookingAndMealStatus())) {
            throw new CanNotChangeOrderTableException(CHANGE_EMPTY_ORDER_ERROR_MESSAGE);
        }
    }
}
