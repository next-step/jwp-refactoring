package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    NONE,
    COOKING,
    MEAL,
    COMPLETION;

    public static List<OrderStatus> excludeCompletionList() {
        return Arrays.asList(COOKING, MEAL);
    }
}
