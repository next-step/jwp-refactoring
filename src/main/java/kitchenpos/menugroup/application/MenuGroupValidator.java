package kitchenpos.menugroup.application;

import org.springframework.stereotype.Component;

import kitchenpos.menu.application.MenuGroupMenuValidator;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;

@Component
public class MenuGroupValidator implements MenuGroupMenuValidator {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateExistsMenuGroupById(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(MenuGroupNotFoundException::new);
    }
}
