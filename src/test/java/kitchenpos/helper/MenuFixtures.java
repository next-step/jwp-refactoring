package kitchenpos.helper;

import static kitchenpos.helper.MenuProductFixtures.반반치킨_메뉴상품_요청;
import static kitchenpos.helper.MenuProductFixtures.통구이_메뉴상품_요청;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

public class MenuFixtures {
    public static MenuRequest 통반세트_메뉴_요청_만들기(Long menuGroupId, Integer price) {
        return 메뉴_요청_만들기(null, "통반세트", price, menuGroupId, Arrays.asList(통구이_메뉴상품_요청, 반반치킨_메뉴상품_요청));
    }

    public static MenuRequest 메뉴_요청_만들기(Long id, String name, Integer price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu 메뉴_만들기(Long id, String name, Integer price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(id, name, new Price(price), menuGroup, menuProducts);
    }

    public static Menu 메뉴_만들기(String name, Integer price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(name, new Price(price), menuGroup, menuProducts);
    }
    public static Menu 메뉴_만들기(Long id, String name, Integer price, MenuProducts menuProducts) {
        return 메뉴_만들기(id, name, price, null, menuProducts);
    }

    public static Menu 메뉴_만들기(Long id, String name, Integer price) {
        return 메뉴_만들기(id, name, price, null, new MenuProducts());
    }

    public static Menu 메뉴_만들기(String name, Integer price) {
        return 메뉴_만들기(null, name, price, null, new MenuProducts());
    }

}
