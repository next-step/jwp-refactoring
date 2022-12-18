package kitchenpos.menu.domain;

import java.util.List;
import java.util.Optional;

public class Menus {
    private final List<Menu> menus;

    private Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public static Menus from(List<Menu> menus) {
        return new Menus(menus);
    }

    public Optional<Menu> findById(Long id) {
        return menus.stream()
            .filter(menu -> menu.hasId(id))
            .findAny();
    }
}
