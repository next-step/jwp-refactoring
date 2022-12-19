package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderValidatorsImpl implements OrderValidators {

    private final OrderValidator orderValidator;
    private final OrderTableStatusValidator orderTableStatusValidator;

    public OrderValidatorsImpl(OrderValidator orderValidator,
                               OrderTableStatusValidator orderTableStatusValidator) {
        this.orderValidator = orderValidator;
        this.orderTableStatusValidator = orderTableStatusValidator;
    }

    @Transactional(readOnly = true)
    public void validateCreation(Order order) {
        orderTableStatusValidator.validate(order.getOrderTableId());
        orderValidator.validate(order);
    }

}
