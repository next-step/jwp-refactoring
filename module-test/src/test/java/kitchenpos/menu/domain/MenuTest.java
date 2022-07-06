package kitchenpos.menu.domain;

import java.math.BigDecimal;

import static kitchenpos.menu.domain.MenuProductsTest.*;

public class MenuTest {

    public static Menu 두마리치킨_메뉴() {
        return new Menu(1L, "두마리치킨", new BigDecimal(30000), 패스트푸드_메뉴그룹(), 두마리치킨_메뉴상품_리스트());
    }

    public static Menu 두마리치킨_메뉴(int price) {
        return new Menu(1L, "두마리치킨", new BigDecimal(price), 패스트푸드_메뉴그룹(), 두마리치킨_메뉴상품_리스트());
    }

    public static Menu 불고기치즈버거_메뉴() {
        return new Menu(2L,"불고기치즈버거", new BigDecimal(6000), 패스트푸드_메뉴그룹(), 불고기치즈버거_메뉴상품_리스트());
    }
}
