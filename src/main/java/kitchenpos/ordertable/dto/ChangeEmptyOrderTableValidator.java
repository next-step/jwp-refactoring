package kitchenpos.ordertable.dto;

import kitchenpos.order.application.OrderStatusService;
import org.springframework.stereotype.Component;

@Component
public class ChangeEmptyOrderTableValidator {
    private final OrderStatusService orderStatusService;

    public ChangeEmptyOrderTableValidator(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public void validate(Long orderTableId) {
        if (orderStatusService.isCookingOrMealStateByOrderTableId(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블의 주문 상태가 조리나 식사일 경우에만 테이블의 빈 유무를 변경할 수 있습니다.");
        }
    }
}
