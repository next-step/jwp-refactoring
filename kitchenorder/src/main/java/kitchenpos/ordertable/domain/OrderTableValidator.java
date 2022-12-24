package kitchenpos.ordertable.domain;

import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderTableConditionValidator orderTableConditionValidator;

    public OrderTableValidator(OrderTableConditionValidator orderTableConditionValidator) {
        this.orderTableConditionValidator = orderTableConditionValidator;
    }

    public void validateEmptyChangeable(OrderTable orderTable) {
        orderTable.checkOrderTableGrouped();
        orderTableConditionValidator.checkNotCompletedOrderExist(orderTable);
    }
}
