package kitchenpos.utils.domain;

import kitchenpos.domain.MenuProduct;

public class MenuProductObjects {
    private final MenuProduct menuProduct1;
    private final MenuProduct menuProduct2;
    private final MenuProduct menuProduct3;
    private final MenuProduct menuProduct4;
    private final MenuProduct menuProduct5;
    private final MenuProduct menuProduct6;

    public MenuProductObjects() {
        menuProduct1 = new MenuProduct();
        menuProduct2 = new MenuProduct();
        menuProduct3 = new MenuProduct();
        menuProduct4 = new MenuProduct();
        menuProduct5 = new MenuProduct();
        menuProduct6 = new MenuProduct();

        menuProduct1.setSeq(1L);
        menuProduct1.setMenuId(1L);
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1L);
        menuProduct2.setSeq(2L);
        menuProduct2.setMenuId(2L);
        menuProduct2.setProductId(2L);
        menuProduct2.setQuantity(1L);
        menuProduct3.setSeq(3L);
        menuProduct3.setMenuId(3L);
        menuProduct3.setProductId(3L);
        menuProduct3.setQuantity(1L);
        menuProduct4.setSeq(4L);
        menuProduct4.setMenuId(4L);
        menuProduct4.setProductId(4L);
        menuProduct4.setQuantity(1L);
        menuProduct5.setSeq(5L);
        menuProduct5.setMenuId(5L);
        menuProduct5.setProductId(5L);
        menuProduct5.setQuantity(1L);
        menuProduct6.setSeq(6L);
        menuProduct6.setMenuId(6L);
        menuProduct6.setProductId(6L);
        menuProduct6.setQuantity(1L);
    }

    public MenuProduct getMenuProduct1() {
        return menuProduct1;
    }

    public MenuProduct getMenuProduct2() {
        return menuProduct2;
    }

    public MenuProduct getMenuProduct3() {
        return menuProduct3;
    }

    public MenuProduct getMenuProduct4() {
        return menuProduct4;
    }

    public MenuProduct getMenuProduct5() {
        return menuProduct5;
    }

    public MenuProduct getMenuProduct6() {
        return menuProduct6;
    }
}
