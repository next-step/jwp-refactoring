package kitchenpos.order.domain;

public class OrderLineItemTest {

    public static OrderLineItem 주문_항목_생성(Long seq, Order order, OrderMenu orderMenu, long quantity) {
        return new OrderLineItem.Builder()
                .seq(seq)
                .order(order)
                .orderMenu(orderMenu)
                .quantity(quantity)
                .build();
    }
}