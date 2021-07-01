package kitchenpos.domain;

import java.util.List;

public class Menus {
    private List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public int size() {
        return menus.size();
    }
}
