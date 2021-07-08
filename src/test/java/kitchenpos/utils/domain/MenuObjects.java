package kitchenpos.utils.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.Menu;

public class MenuObjects {
    private final MenuProductObjects menuProductObjects;
    private final Menu menu1;
    private final Menu menu2;
    private final Menu menu3;
    private final Menu menu4;
    private final Menu menu5;
    private final Menu menu6;

    public MenuObjects() {
        menuProductObjects = new MenuProductObjects();
        menu1 = new Menu();
        menu2 = new Menu();
        menu3 = new Menu();
        menu4 = new Menu();
        menu5 = new Menu();
        menu6 = new Menu();

        menu1.setId(1L);
        menu1.setName("후라이드치킨");
        menu1.setPrice(BigDecimal.valueOf(16000.00));
        menu1.setMenuGroupId(2L);
        menu2.setId(2L);
        menu2.setName("양념치킨");
        menu2.setPrice(BigDecimal.valueOf(16000.00));
        menu2.setMenuGroupId(2L);
        menu3.setId(3L);
        menu3.setName("반반치킨");
        menu3.setPrice(BigDecimal.valueOf(16000.00));
        menu3.setMenuGroupId(2L);
        menu4.setId(4L);
        menu4.setName("통구이");
        menu4.setPrice(BigDecimal.valueOf(16000.00));
        menu4.setMenuGroupId(2L);
        menu5.setId(5L);
        menu5.setName("간장치킨");
        menu5.setPrice(BigDecimal.valueOf(17000.00));
        menu5.setMenuGroupId(2L);
        menu6.setId(6L);
        menu6.setName("순살치킨");
        menu6.setPrice(BigDecimal.valueOf(17000.00));
        menu6.setMenuGroupId(2L);
    }

    public Menu getMenu1() {
        return menu1;
    }

    public Menu getMenu2() {
        return menu2;
    }

    public Menu getMenu3() {
        return menu3;
    }

    public Menu getMenu4() {
        return menu4;
    }

    public Menu getMenu5() {
        return menu5;
    }

    public Menu getMenu6() {
        return menu6;
    }

    public List<Menu> getMenus() {
        return new ArrayList<>(Arrays.asList(menu1, menu2, menu3, menu4, menu5, menu6));
    }
}
