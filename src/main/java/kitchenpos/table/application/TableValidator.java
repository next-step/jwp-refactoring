package kitchenpos.table.application;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Orders;

@Component
public class TableValidator {
    private final OrderService orderService;
    
    public TableValidator(OrderService orderService) {
        this.orderService = orderService;
    }
    
    public void checkIsCookingOrMeal(Long orderTableId) {
        Orders orders = Orders.from(orderService.findAllByOrderTableId(orderTableId));
        if (orders.isContainsMealStatus() || orders.isContainsCookingStatus()) {
            throw new IllegalArgumentException("조리중, 식사중인 주문 테이블은 변경할 수 없습니다");
        }
    }
}
