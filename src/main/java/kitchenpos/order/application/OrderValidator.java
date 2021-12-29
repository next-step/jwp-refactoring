package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void validateCreatable(final Order order) {
        if (order.getOrderTable().isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문을 등록할 수 없습니다.");
        }
    }

    public void validateStatusChangeable(final Order order) {
        if (order.isCompleted()) {
            throw new IllegalArgumentException("이미 완료(COMPLETION) 된 주문입니다.");
        }
    }
}
