package kitchenpos.domain;

public class OrderLineItemTest {

    public static OrderLineItem 주문_항목_생성(Order order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}
