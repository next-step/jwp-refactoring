package kitchenpos.order.sample;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.order.domain.OrderLineItemMenu;

public class OrderLineItemMenuSample {

    public static OrderLineItemMenu 이십원_후라이트치킨_두마리세트_주문_메뉴() {
        return OrderLineItemMenu.of(1L,
            Name.from("후라이드치킨_두마리세트"),
            Price.from(BigDecimal.TEN));
    }
}
