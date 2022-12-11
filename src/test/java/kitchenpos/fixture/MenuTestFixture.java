package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuGroupTestFixture.중국집_1인_메뉴_세트;
import static kitchenpos.fixture.MenuGroupTestFixture.중국집_1인_메뉴_세트_요청;
import static kitchenpos.fixture.MenuProductTestFixture.*;

public class MenuTestFixture {

    public static MenuRequest createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return MenuRequest.of(id, name, price, menuGroupId, menuProducts);
    }

    public static MenuRequest createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return MenuRequest.of(null, name, price, menuGroupId, menuProducts);
    }

    public static MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청() {
        return createMenu(2L, "짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), 중국집_1인_메뉴_세트(중국집_1인_메뉴_세트_요청()).getId(),
                Arrays.asList(짬뽕메뉴상품(), 탕수육메뉴상품(), 단무지메뉴상품()));
    }

    public static MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청() {
        return createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집_1인_메뉴_세트(중국집_1인_메뉴_세트_요청()).getId(),
                Arrays.asList(짜장면메뉴상품(), 탕수육메뉴상품(), 단무지메뉴상품()));
    }

    public static Menu 메뉴_세트_생성(MenuRequest request) {
        return Menu.of(request.getId(), request.getName(), request.getPrice(), request.getMenuGroupId(), request.getMenuProducts());
    }
}
