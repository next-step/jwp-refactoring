package kitchenpos.order.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isRestrictedChangeEmpty() {
        return Arrays.asList(COOKING, MEAL).contains(this);
    }

    public boolean isRestrictedChangeOrderStatus() {
        return COMPLETION.equals(this);
    }
}
