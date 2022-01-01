package kitchenpos.moduledomain.common;


import kitchenpos.moduledomain.menu.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 콜라_1개() {
        return MenuProduct.of(1L, ProductFixture.콜라().getId(), 1L);
    }

    public static MenuProduct 양념치킨_1개() {
        return MenuProduct.of(2L, ProductFixture.양념치킨().getId(), 1L);
    }

    public static MenuProduct 후라이드_1개() {
        return MenuProduct.of(3L, ProductFixture.후라이드().getId(), 1L);
    }

    public static MenuProduct 떡볶이_1개() {
        return MenuProduct.of(4L, ProductFixture.떡볶이().getId(), 1L);
    }


    public static MenuProduct 가격이없는_반반치킨_1개() {
        return MenuProduct.of(5L, ProductFixture.반반치킨().getId(), 1L);
    }

}
