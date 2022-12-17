package kitchenpos.validator.menu.impl;

import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.validator.menu.MenuValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class AlreadyGroupedMenuValidator extends MenuValidator {

    private final MenuGroupRepository menuGroupRepository;

    public AlreadyGroupedMenuValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    protected void validate(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "메뉴 등록시, 등록되어 있는 메뉴 그룹만 지정할 수 있습니다[menuGroupId:" + menuGroupId + "]"));
    }
}
