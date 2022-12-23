package order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING("조리"), MEAL("식사"), COMPLETION("계산완료");

    private final String name;

    OrderStatus(final String name) {
        this.name = name;
    }

    public static List<OrderStatus> onGoingOrderStatus() {
        return Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    }
}
