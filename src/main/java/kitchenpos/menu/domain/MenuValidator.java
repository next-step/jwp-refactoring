package kitchenpos.menu.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.repository.MenuGroupRepository;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateCreate(Menu menu) {
        validateExistMenuGroup(menu.getMenuGroupId());
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId).orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));
    }
}
