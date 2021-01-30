package kitchenpos.order.domain.order;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus of(final String requestOrderStatus) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> requestOrderStatus.equalsIgnoreCase(orderStatus.name()))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }

    public boolean isCookingOrMeal() {
        return this == COOKING || this == MEAL;
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
