package kitchenpos.order;


import com.google.common.collect.Lists;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static final List<OrderStatus> UNGROUP_IMPOSSIBLE_ORDER_STATUS =
        Lists.newArrayList(OrderStatus.COOKING, OrderStatus.MEAL);

    public static final List<OrderStatus> CHANGE_EMPTY_IMPOSSIBLE_ORDER_STATUS =
        Lists.newArrayList(OrderStatus.COOKING, OrderStatus.MEAL);
}
