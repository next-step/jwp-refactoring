package kitchenpos.order.domain;

import kitchenpos.menu.application.MenuService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuCountOrderValidator {
    private final MenuService menuService;

    public MenuCountOrderValidator(MenuService menuService) {
        this.menuService = menuService;
    }

    public void validate(List<Long> menuIds) {
        if (menuIds.size() != menuService.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
