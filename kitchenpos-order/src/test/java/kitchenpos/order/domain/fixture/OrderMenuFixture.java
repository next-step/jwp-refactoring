package kitchenpos.order.domain.fixture;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.order.domain.OrderMenu;

import java.math.BigDecimal;

public class OrderMenuFixture {

    public static OrderMenu orderMenu() {
        return OrderMenu.of(1L, new Name("A"), new Price(BigDecimal.ONE));
    }
}
