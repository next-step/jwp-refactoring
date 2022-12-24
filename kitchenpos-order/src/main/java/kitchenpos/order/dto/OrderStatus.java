package kitchenpos.order.dto;

public class OrderStatus {
    private String orderStatus;

    protected OrderStatus() {}

    private OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatus of(String orderStatus) {
        return new OrderStatus(orderStatus);
    }

    public kitchenpos.order.domain.OrderStatus getOrderStatus() {
        return kitchenpos.order.domain.OrderStatus.valueOf(orderStatus);
    }
}
