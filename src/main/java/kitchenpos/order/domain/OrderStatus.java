package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(String s) {
        return OrderStatus.valueOf(s.toUpperCase());
    }

    public static List<OrderStatus> getCookingAndMeal() {
        return Arrays.asList(COOKING, MEAL);
    }
}
