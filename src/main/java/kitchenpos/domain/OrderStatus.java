package kitchenpos.domain;

import kitchenpos.dto.OrderRequest;

import java.util.Objects;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompleted(OrderStatus orderStatus) {
        return OrderStatus.COMPLETION == orderStatus;
    }
}
