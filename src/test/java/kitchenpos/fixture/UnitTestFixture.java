package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

public class UnitTestFixture {
    public final MenuGroup 구이류 = new MenuGroup(1L, "구이류");
    public final MenuGroup 식사류 = new MenuGroup(2L, "식사류");

    public final Product 삼겹살 = new Product(1L, "삼겹살", BigDecimal.valueOf(14000L));
    public final Product 목살 = new Product(2L, "목살", BigDecimal.valueOf(15000L));
    public final Product 김치찌개 = new Product(3L, "김치찌개", BigDecimal.valueOf(8000));
    public final Product 공깃밥 = new Product(4L, "공깃밥", BigDecimal.valueOf(1000));
}
