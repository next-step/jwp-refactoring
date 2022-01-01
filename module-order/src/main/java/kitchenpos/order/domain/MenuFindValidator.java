package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuFindValidator {
    private final MenuRepository menuRepository;

    public MenuFindValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateMenu(Long menuId) {
        menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴 정보가 없습니다."));
    }
}
