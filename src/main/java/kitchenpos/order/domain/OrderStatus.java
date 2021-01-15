package kitchenpos.order.domain;

import kitchenpos.common.exception.NotFoundException;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    OrderStatus() {
    }

    public static OrderStatus matchOrderStatus(final String status) {
        return Arrays.stream(values())
            .filter(orderStatus -> orderStatus.name().equals(status.toUpperCase()))
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
