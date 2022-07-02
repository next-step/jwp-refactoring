package kitchenpos.menu.domain;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Menus {
    private final Map<Long, Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus.stream()
                .collect(collectingAndThen(toMap(Menu::getId, Function.identity()), Collections::unmodifiableMap));
    }

    public Menu getMenuBy(Long menuId) {
        if (!menus.containsKey(menuId)) {
            throw new IllegalStateException("[ERROR] menuId 대한 값이 없습니다.");
        }
        return menus.get(menuId);
    }
}
