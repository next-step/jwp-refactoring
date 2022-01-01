package kitchenpos.menu.domain;

import kitchenpos.order.domain.MenuFindValidator;
import org.springframework.stereotype.Component;

@Component
public class MenuFindValidatorImpl implements MenuFindValidator {
    private final MenuRepository menuRepository;

    public MenuFindValidatorImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void validateMenu(Long menuId) {
        menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴 정보가 없습니다."));
    }
}
