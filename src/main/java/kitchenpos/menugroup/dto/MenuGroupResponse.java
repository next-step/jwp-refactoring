package kitchenpos.menugroup.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupResponse {
    private final Long id;
    private final String name;

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
