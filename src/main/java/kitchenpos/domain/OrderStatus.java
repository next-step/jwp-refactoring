package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static final List<OrderStatus> NOT_COMPLETED_LIST = Arrays.asList(COOKING, MEAL);

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
