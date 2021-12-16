package kitchenpos.domain.menu;

import java.util.List;

import kitchenpos.exception.menu.NotFoundMenuException;

public class Menus {
    List<Menu> menus;

    private Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public static Menus of(List<Menu> menus) {
        return new Menus(menus);
    }

    public Menu findById(Long menuId) {
        return this.menus.stream()
                        .filter(menu -> menu.isEqualMenuId(menuId))
                        .findFirst()
                        .orElseThrow(NotFoundMenuException::new);
    }

    public boolean contains(Menu menu) {
        return this.menus.contains(menu);
    }
}
