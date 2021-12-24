package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isComplete() {
        return this.equals(COMPLETION);
    }

    public static List<OrderStatus> excludeCompletionValues() {
        return Arrays.stream(values())
            .filter(orderStatus -> !orderStatus.isComplete())
            .collect(Collectors.toList());
    }
}
