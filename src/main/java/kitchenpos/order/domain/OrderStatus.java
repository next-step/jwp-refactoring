package kitchenpos.order.domain;

import java.util.Objects;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompletion(String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)){
            return true;
        }
        return false;
    }
}
