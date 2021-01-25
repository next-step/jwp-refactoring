package kitchenpos.domain.order;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus getOrderStatus(String status) {
        return Arrays.stream(OrderStatus.values()).filter(o -> o.name().equals(status))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
