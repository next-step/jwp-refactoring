package kitchenpos.menu.domain;

import java.util.List;

public class Menus {
    private final List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public Menu getMenuBy(Long menuId) {
        return menus.stream()
                .filter(menu -> menuId.equals(menu.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("[ERROR] 메뉴 변환에 오류가 발생하였습니다."));
    }
}
