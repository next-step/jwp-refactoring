package kitchenpos.domain;

public class OrderLineItemTest {

    public static OrderLineItem 주문_항목_생성(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem.Builder()
                .seq(seq)
                .orderId(orderId)
                .menuId(menuId)
                .quantity(quantity)
                .build();
    }
}