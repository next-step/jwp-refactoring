package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<OrderStatus> getInProgressStatuses() {
        return Arrays.asList(COOKING, MEAL);
    }

    public boolean isCompleted() {
        return this.equals(COMPLETION);
    }

    public boolean inProgress() {
        return !this.equals(COMPLETION);
    }
}
