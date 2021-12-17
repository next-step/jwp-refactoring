package common;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    public static MenuProduct 후라이드_1개() {
        return new MenuProduct(1L, 1L, 1L);
    }

    public static MenuProduct 양념치킨_1개() {
        return new MenuProduct(2L, 2L, 1L);
    }

    public static MenuProduct 가격이없는_반반치킨_1개() {
        return new MenuProduct(3L, 3L, 1L);
    }

    public static MenuProduct 반반치킨_1개() {
        return new MenuProduct(4L, 4L, 1L);
    }
}
