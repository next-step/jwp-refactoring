package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompletionOrders {
    
    private final List<Order> orders;
    
    protected CompletionOrders() {
        orders = new ArrayList<Order>();
    }
    
    private CompletionOrders(List<Order> orders) {
        validateCompletionOrders(orders);
        this.orders = orders;
    }

    public static CompletionOrders from(List<Order> orders) {
        
        return new CompletionOrders(orders);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }
    
    private void validateCompletionOrders(List<Order> orders) {
        if (isContainsMealStatus(orders) || isContainsCookingStatus(orders)) {
            throw new IllegalArgumentException("조리중, 식사중인 주문 테이블은 변경할 수 없습니다");
        }
    }

    private boolean isContainsMealStatus(List<Order> orders) {
        return orders.stream()
                .anyMatch(Order::isMeal);
    }
    
    private boolean isContainsCookingStatus(List<Order> orders) {
        return orders.stream()
                .anyMatch(Order::isCooking);
    }
    
}
