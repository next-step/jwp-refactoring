package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static Menu 메뉴_요청_데이터_생성(String name, BigDecimal menuPrice, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, name, menuPrice, menuGroupId, menuProducts);
    }

    public static Menu 메뉴_데이터_생성(Long id, String name, BigDecimal menuPrice, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, menuPrice, menuGroupId, menuProducts);
    }

}
