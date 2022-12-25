package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.Name;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.Price;

import java.math.BigDecimal;

public class OrderMenuFixture {

    public static OrderMenu orderMenu() {
        return OrderMenu.of(1L, new Name("A"), new Price(BigDecimal.ONE));
    }
}
