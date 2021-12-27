package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import org.assertj.core.util.Lists;

import java.util.List;

import static kitchenpos.fixture.MenuFixture.menu1;
import static kitchenpos.fixture.MenuFixture.menu2;
import static kitchenpos.fixture.ProductFixture.후라이드치킨;
import static kitchenpos.fixture.ProductFixture.양념치킨;

public class MenuProductsFixture {
    public static final List<MenuProduct> menuProducts1 = Lists.newArrayList(MenuProduct.of(1L, menu1.getId(), 후라이드치킨.getId(), 1L));
    public static final List<MenuProduct> menuProducts2 = Lists.newArrayList(MenuProduct.of(2L, menu2.getId(), 양념치킨.getId(), 1L));;
}
