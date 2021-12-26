package kitchenpos.order.domain;

import com.google.common.collect.Lists;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static final List<OrderStatus> UNGROUP_DISABLE_ORDER_STATUS =
        Lists.newArrayList(OrderStatus.COOKING, OrderStatus.MEAL);
}
