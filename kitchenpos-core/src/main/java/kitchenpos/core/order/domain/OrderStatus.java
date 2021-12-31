package kitchenpos.core.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<OrderStatus> getCookingAndMealStatus() {
        return Arrays.asList(COOKING, MEAL);
    }
}
