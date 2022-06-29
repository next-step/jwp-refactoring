package kitchenpos.application;

import kitchenpos.domain.*;

import java.math.BigDecimal;

public class CommonTestFixture {
    public static Product createProduct(Long id, String name, int price) {
        return new Product(id, name, BigDecimal.valueOf(price));
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
