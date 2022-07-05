package kitchenpos.domain.order;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<OrderStatus> cnaNotChangeOrderTableStatuses() {
        return Arrays.asList(COOKING, MEAL);
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
