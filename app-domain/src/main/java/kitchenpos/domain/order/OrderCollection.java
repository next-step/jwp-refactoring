package kitchenpos.domain.order;

import java.util.List;

public class OrderCollection {
    private List<Orders> orders;

    public OrderCollection(List<Orders> orders) {
        this.orders = orders;
    }

    public void checkOrderRequest() {
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("주문을 받지 않은 테이블 입니다");
        }
    }

    public void checkOrderStatus() {
        if (!orders.stream().allMatch(Orders::isCompletion)) {
            throw new IllegalArgumentException("식사완료 상태가 아닌 주문이 존재합니다");
        }
    }
}
