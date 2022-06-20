package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuFixture {

    public static Menu 메뉴_요청_데이터_생성(String name, BigDecimal menuPrice, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, name, menuPrice, menuGroupId, menuProducts);
    }

    public static Menu 메뉴_데이터_생성(Long id, String name, BigDecimal menuPrice, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, menuPrice, menuGroupId, menuProducts);
    }

    public static void 메뉴_데이터_확인(Menu menu, Long id, String name, Long menuGroupId, BigDecimal menuPrice) {
        assertAll(
                () -> assertEquals(id, menu.getId()),
                () -> assertEquals(name, menu.getName()),
                () -> assertEquals(menuPrice, menu.getPrice()),
                () -> assertEquals(menuGroupId, menu.getMenuGroupId()),
                () -> assertThat(menu.getMenuProducts()).isNotEmpty()
        );
    }
}
