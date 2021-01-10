package kitchenpos.infra.orderTable;

import kitchenpos.application.OrderService;
import kitchenpos.domain.orderTable.SafeOrder;
import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeEmptyException;
import org.springframework.stereotype.Component;

@Component
public class OrderAdapter implements SafeOrder {
    private final OrderService orderService;

    public OrderAdapter(final OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void canChangeEmptyStatus(final Long orderTableId) {
        if (orderService.isThisTableInMealOrCooking(orderTableId)) {
            throw new InvalidTryChangeEmptyException("조리중이거나 식사중인 주문 테이블의 비움 상태를 바꿀 수 없습니다.");
        }
    }
}
