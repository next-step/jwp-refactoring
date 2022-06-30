package kitchenpos.menu.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menus {
    private final Map<Long, Menu> menuCaches = new HashMap<>();
    private final List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public Menu getMenuBy(Long menuId) {
        return menuCaches.computeIfAbsent(menuId, this::getMenu);
    }

    private Menu getMenu(Long menuId) {
        return menus.stream()
                .filter(menu -> menuId.equals(menu.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("[ERROR] 메뉴 변환에 오류가 발생하였습니다."));
    }
}
