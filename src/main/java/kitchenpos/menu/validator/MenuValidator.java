package kitchenpos.menu.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroup(findMenuGroupById(menuRequest.getMenuGroupId()));
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if(menuGroup == null) {
            throw new IllegalArgumentException(ErrorCode.메뉴_그룹은_비어있을_수_없음.getErrorMessage());
        }
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_메뉴_그룹.getErrorMessage()));
    }
}
