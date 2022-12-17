package kitchenpos.menu.validator;

import java.util.Objects;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroup(findMenuGroupById(menuRequest.getMenuGroupId()));
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(ErrorCode.MENU_GROUP_NOT_EMPTY.getErrorMessage());
        }
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new NotFoundException());
    }
}
