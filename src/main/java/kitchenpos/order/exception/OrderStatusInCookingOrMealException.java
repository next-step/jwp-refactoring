package kitchenpos.order.exception;

public class OrderStatusInCookingOrMealException extends IllegalArgumentException {

    public OrderStatusInCookingOrMealException() {
        super("주문상태가 식사또는 요리 상태입니다.");
    }

}
