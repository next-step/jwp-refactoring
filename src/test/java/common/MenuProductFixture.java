package common;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {
    public static MenuProduct 후라이드_1개() {

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setQuantity(1L);

        return menuProduct;
    }

    public static MenuProduct 양념치킨_1개() {

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(2L);
        menuProduct.setMenuId(2L);
        menuProduct.setQuantity(1L);

        return menuProduct;
    }

    public static MenuProduct 가격이없는_반반치킨_1개() {

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(3L);
        menuProduct.setMenuId(3L);
        menuProduct.setQuantity(1L);
        return menuProduct;
    }

    public static MenuProduct 반반치킨_1개() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(4L);
        menuProduct.setMenuId(4L);
        menuProduct.setQuantity(1L);
        return menuProduct;
    }
}
