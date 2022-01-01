package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.CompletionOrders;
import kitchenpos.order.domain.Order;

@Component
public class TableValidator {
    private final OrderService orderService;
    
    public TableValidator(OrderService orderService) {
        this.orderService = orderService;
    }
    
    public void checkIsCookingOrMeal(Long orderTableId) {
        List<Order> orders = orderService.findAllByOrderTableId(orderTableId);
        CompletionOrders.from(orders);
    }
}
