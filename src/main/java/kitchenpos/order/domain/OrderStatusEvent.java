package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTableStatusEvent;
import kitchenpos.table.domain.TableStatus;

public class OrderStatusEvent extends OrderTableStatusEvent {

    private OrderStatusEvent(Object source, Long orderTableId, Long orderId,
        TableStatus tableStatus) {
        super(source, orderTableId, orderId, tableStatus);
    }

    public static OrderStatusEvent of(Object source, Order order) {
        return new OrderStatusEvent(source, order.getOrderTableId(), order.getId(),
            convertTableStatus(order.getOrderStatus()));
    }

    private static TableStatus convertTableStatus(OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION.equals(orderStatus)) {
            return TableStatus.COMPLETION;
        }
        return TableStatus.ORDERED;
    }

    @Override
    public Long getOrderTableId() {
        return this.orderTableId;
    }

    @Override
    public TableStatus getTableStatus() {
        return this.tableStatus;
    }

    @Override
    public Long getOrderId() {
        return this.orderId;
    }
}
