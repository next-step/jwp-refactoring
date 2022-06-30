package kitchenpos.domain;

public class OrderLineItemTest {

    public static OrderLineItem 주문_항목_생성(Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
