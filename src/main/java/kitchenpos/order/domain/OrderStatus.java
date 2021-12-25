package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.Collection;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static Collection<OrderStatus> getNotCompletions() {
        return Arrays.asList(COOKING, MEAL);
    }
}
