package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.domain.MenuGroupTestFixture.중국집_1인_메뉴_세트;
import static kitchenpos.domain.MenuProductTestFixture.*;

public class MenuTestFixture {

    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(null, name, price, menuGroupId, menuProducts);
    }

    public static Menu 짬뽕_탕수육_1인_메뉴_세트() {
        return createMenu(2L, "짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), 중국집_1인_메뉴_세트().getId(),
                Arrays.asList(짬뽕상품(), 탕수육상품(), 단무지상품()));
    }

    public static Menu 짜장면_탕수육_1인_메뉴_세트() {
        return createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집_1인_메뉴_세트().getId(),
                Arrays.asList(짜장면상품(), 탕수육상품(), 단무지상품()));
    }
}
