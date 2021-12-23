package kitchenpos.order.domain;

import static java.util.Arrays.asList;

import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isEqualsCompletion(final OrderStatus changeOrderStatus) {
        return COMPLETION.equals(changeOrderStatus);
    }

    public static void validStatusIsCookingOrMealThrow(final OrderStatus orderStatus) {
        if (asList(COOKING, MEAL).contains(orderStatus)) {
            throw new IllegalArgumentException("주문 상태가 조리 또는 식사 상태인 경우에는 단체지정할 수 없습니다.");
        }
    }

    public static void validStatusIsCookingOrMealThrow(final List<OrderStatus> orderStatus) {
        orderStatus.stream()
                .forEach(OrderStatus::validStatusIsCookingOrMealThrow);
    }
}
