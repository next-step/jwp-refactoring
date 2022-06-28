package kitchenpos.domain.order;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static final List<OrderStatus> STARTED_ORDER_READY_STATUS = Arrays.asList(COOKING, MEAL);

    public boolean isCompletion() {
        return this == COMPLETION;
    }

}
