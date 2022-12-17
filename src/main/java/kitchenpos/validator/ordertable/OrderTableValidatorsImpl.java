package kitchenpos.validator.ordertable;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.validator.OrderTableValidators;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderTableValidatorsImpl implements OrderTableValidators {

    private final List<OrderTableValidator> orderTableValidators;

    public OrderTableValidatorsImpl(List<OrderTableValidator> orderTableValidators) {
        this.orderTableValidators = orderTableValidators;
    }

    @Transactional(readOnly = true)
    public void validateChangeEmpty(OrderTable orderTable) {
        orderTableValidators.forEach(orderTableValidator -> orderTableValidator.validate(orderTable));
    }

}
