package common;

import static common.ProductFixture.떡볶이;
import static common.ProductFixture.반반치킨;
import static common.ProductFixture.양념치킨;
import static common.ProductFixture.콜라;
import static common.ProductFixture.후라이드;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {


    public static MenuProduct 콜라_1개(Menu menu) {
        return MenuProduct.of(1L, menu, 콜라(), 1L);
    }

    public static MenuProduct 양념치킨_1개(Menu menu) {
        return MenuProduct.of(2L, menu, 양념치킨(), 1L);
    }

    public static MenuProduct 후라이드_1개(Menu menu) {
        return MenuProduct.of(3L, menu, 후라이드(), 1L);
    }

    public static MenuProduct 떡볶이_1개(Menu menu) {
        return MenuProduct.of(4L, menu, 떡볶이(), 1L);
    }


    public static MenuProduct 가격이없는_반반치킨_1개(Menu menu) {
        return MenuProduct.of(5L, menu, 반반치킨(), 1L);
    }

}
