package common;

import static common.MenuFixture.메뉴_가격이없는_반반치킨;
import static common.MenuFixture.메뉴_반반치킨;
import static common.MenuFixture.메뉴_양념치킨;
import static common.MenuFixture.메뉴_후라이드;
import static common.ProductFixture.반반치킨;
import static common.ProductFixture.양념치킨;
import static common.ProductFixture.콜라;
import static common.ProductFixture.후라이드;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {


    public static MenuProduct 콜라_1개(Menu menu) {
        return MenuProduct.of(menu, 콜라(), 1L);
    }

    public static MenuProduct 양념치킨_1개(Menu menu) {
        return MenuProduct.of(menu, 양념치킨(), 1L);
    }

    public static MenuProduct 가격이없는_반반치킨_1개(Menu menu) {
        return MenuProduct.of(menu, 반반치킨(), 1L);
    }

}
