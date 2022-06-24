package kitchenpos.helper;

import static kitchenpos.helper.MenuGroupFixtures.두마리메뉴_그룹;
import static kitchenpos.helper.MenuProductFixtures.반반치킨_메뉴상품_요청;
import static kitchenpos.helper.MenuProductFixtures.통구이_메뉴상품_요청;

import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;

public class MenuFixtures {
    public static MenuRequest 통반세트_요청_만들기(Integer price){
        return new MenuRequest(null, "통반세트", price, 두마리메뉴_그룹.getId(), Arrays.asList(통구이_메뉴상품_요청, 반반치킨_메뉴상품_요청));
    }

    public static Menu 메뉴_만들기(Long id, String name, Integer price, MenuGroup menuGroup, MenuProducts menuProducts){
        return new Menu(id, name, price, menuGroup, menuProducts);
    }
    public static Menu 메뉴_만들기(Long id, String name, Integer price, MenuProducts menuProducts){
        return 메뉴_만들기(id, name, price, null, menuProducts);
    }
    public static Menu 메뉴_만들기(Long id, String name, Integer price){
        return 메뉴_만들기(id, name, price, null, null);
    }

}
