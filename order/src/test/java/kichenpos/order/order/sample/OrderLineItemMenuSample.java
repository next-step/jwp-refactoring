package kichenpos.order.order.sample;

import java.math.BigDecimal;
import kichenpos.common.domain.Name;
import kichenpos.common.domain.Price;
import kichenpos.order.order.domain.OrderLineItemMenu;

public class OrderLineItemMenuSample {

    public static OrderLineItemMenu 이십원_후라이트치킨_두마리세트_주문_메뉴() {
        return OrderLineItemMenu.of(1L,
            Name.from("후라이드치킨_두마리세트"),
            Price.from(BigDecimal.TEN));
    }
}
