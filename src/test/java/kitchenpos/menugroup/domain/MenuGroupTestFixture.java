package kitchenpos.menugroup.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class MenuGroupTestFixture {

    public static MenuGroup create(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup create(String name) {
        return new MenuGroup(null, name);
    }
}
