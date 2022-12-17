package kitchenpos.validator.order;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.validator.OrderValidators;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderValidatorsImpl implements OrderValidators {

    private final List<OrderValidator> orderValidators;

    public OrderValidatorsImpl(List<OrderValidator> orderValidators) {
        this.orderValidators = orderValidators;
    }

    @Transactional(readOnly = true)
    public void validateCreation(Order order) {
        orderValidators.forEach(orderValidator -> orderValidator.validate(order));
    }

}
