package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuGroupTestFixture.세트류;
import static kitchenpos.menu.domain.MenuProductTestFixture.*;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

public class MenuTestFixture {
    public static MenuRequest 짜장_탕수육_세트_요청 = menuRequest(
            "짜장_탕수육_세트", BigDecimal.valueOf(25_000L), 3L, 짜장면_1그릇_요청, 탕수육_소_1그릇_요청);
    public static Menu 짜장_탕수육_세트 = menu("짜장_탕수육_세트", BigDecimal.valueOf(25_000L), 세트류, 짜장면_1그릇, 탕수육_소_1그릇);

    public static MenuRequest menuRequest(String name, BigDecimal price, Long menuGroupId, MenuProductRequest... menuProducts) {
        return new MenuRequest(name, price, menuGroupId, Arrays.asList(menuProducts));
    }

    public static Menu menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, MenuProduct... menuProducts) {
        return Menu.of(id, name, price, menuGroup, Arrays.asList(menuProducts));
    }

    public static Menu menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProduct... menuProducts) {
        return Menu.of(name, price, menuGroup, Arrays.asList(menuProducts));
    }
}
