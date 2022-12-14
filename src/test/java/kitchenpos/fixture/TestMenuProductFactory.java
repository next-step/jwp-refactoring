package kitchenpos.fixture;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

public class TestMenuProductFactory {
    public static MenuProduct create() {
        Product product = new Product(1L, new Name("짬뽕"), new Price(BigDecimal.valueOf(8_000)));
        return new MenuProduct(new Quantity(1L), product);
    }
}
