package kitchenpos.utils.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kitchenpos.menu.domain.Menu;

public class MenuObjects {
    private final Menu menu1;
    private final Menu menu2;
    private final Menu menu3;
    private final Menu menu4;
    private final Menu menu5;
    private final Menu menu6;

    public MenuObjects() {
        menu1 = new Menu();
        menu2 = new Menu();
        menu3 = new Menu();
        menu4 = new Menu();
        menu5 = new Menu();
        menu6 = new Menu();
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
