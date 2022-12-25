package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Menus {
    private final List<Menu> menus;

    private Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public static Menus from(List<Menu> menus) {
        return new Menus(menus);
    }

    public List<OrderMenu> generateOrderMenus() {
        return menus.stream()
            .map(menu -> OrderMenu.generate(menu.getId(), menu.getName(), menu.getPrice()))
            .collect(Collectors.toList());
    }
}
