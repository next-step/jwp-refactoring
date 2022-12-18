package kitchenpos.validator.menu.impl;

import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.validator.menu.MenuCreationValidator;
import org.springframework.stereotype.Component;

@Component
public class AlreadyGroupedMenuValidator implements MenuCreationValidator {

    private final MenuGroupRepository menuGroupRepository;

    public AlreadyGroupedMenuValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public void validate(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "메뉴 등록시, 등록되어 있는 메뉴 그룹만 지정할 수 있습니다[menuGroupId:" + menuGroupId + "]"));
    }
}
