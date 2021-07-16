package kitchenpos.table.event;

import kitchenpos.order.domain.Order;

import java.util.List;

public class OrderTableChangedEmptyValidator {
    public OrderTableChangedEmptyValidator(List<Order> orders){
        validOrderStatusCompletion(orders);
    }

    private void validOrderStatusCompletion(List<Order> orders){
        if (!isOrderStatusCompletion(orders)) {
            throw new IllegalArgumentException("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다.");
        }
    }
    private boolean isOrderStatusCompletion(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.isOrderStatusCompletion())
                .findFirst()
                .isPresent();
    }
}
