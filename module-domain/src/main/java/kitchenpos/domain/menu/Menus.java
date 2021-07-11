package kitchenpos.domain.menu;

import kitchenpos.common.exception.EntityNotExistsException;

import java.util.ArrayList;
import java.util.List;

public class Menus {
    private List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = new ArrayList<>(menus);
    }

    public int size() {
        return menus.size();
    }

    public Menu findById(long menuId) {
        return menus.stream()
                .filter(item -> item.getId().equals(menuId))
                .findFirst()
                .orElseThrow(EntityNotExistsException::new);
    }
}
