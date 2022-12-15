package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;

public class MenuFixture {

    public static final Menu 후라이드치킨 = 후라이드치킨();

    private static Menu 후라이드치킨() {
        return new Menu("후라이드치킨",
                BigDecimal.valueOf(16_000),
                null,
                Arrays.asList(new MenuProduct(null, null, 2)));
    }
}
