package kitchenpos.menu.domain;

import kitchenpos.ExceptionMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MenuValidator {

    private MenuRepository menuRepository;

    public MenuValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Map<Long, Menu> checkExistMenuIds(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException(ExceptionMessage.NOT_EXIST_MENU.getMessage());
        }
        return menus.stream().collect(Collectors.toMap(Menu::getId, menu -> menu, (id1, id2) -> id1));
    }
}
