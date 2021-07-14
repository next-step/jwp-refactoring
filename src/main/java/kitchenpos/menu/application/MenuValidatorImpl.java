package kitchenpos.menu.application;

import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_MENU;

import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.MenuValidator;
import org.springframework.stereotype.Component;

@Component
public class MenuValidatorImpl implements MenuValidator {

    private final MenuRepository menuRepository;

    public MenuValidatorImpl(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void checkExistsMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new KitchenposException(NOT_FOUND_MENU);
        }
    }
}
