package kitchenpos.domain;

import kitchenpos.exception.EntityNotExistsException;

import java.util.List;

public class Menus {
    private List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
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
