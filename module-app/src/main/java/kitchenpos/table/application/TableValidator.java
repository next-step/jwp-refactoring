package kitchenpos.table.application;

import java.util.Optional;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.Order;
import kitchenpos.common.domain.OrderStatus;
import kitchenpos.common.exception.CannotTableChangeEmptyException;

@Component
public class TableValidator {
    public void validateExistsOrderStatusIsCookingANdMeal(Optional<Order> orderOptional) {
        orderOptional.ifPresent(this::validateOrderStatus);
    }

    private void validateOrderStatus(Order order) {
        if (order.equalsOrderStatus(OrderStatus.COOKING) || order.equalsOrderStatus(OrderStatus.MEAL)) {
            throw new CannotTableChangeEmptyException("주문 상태가 COOKING 또는 MEAL인 주문이 존재합니다. 입력 ID : " + order.getId());
        }
    }
}
