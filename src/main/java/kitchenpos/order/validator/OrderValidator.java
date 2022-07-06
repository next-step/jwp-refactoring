package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void isPossibleChangeOrderStatus(final Order order) {
        if (OrderStatus.COMPLETION.equals(order.getOrderStatus())) {
            throw new IllegalArgumentException("완료 상태의 주문의 상태를 변경할 수 없습니다.");
        }
    }
}
