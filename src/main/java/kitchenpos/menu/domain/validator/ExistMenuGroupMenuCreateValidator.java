package kitchenpos.menu.domain.validator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.exception.NotFoundMenuGroupException;
import kitchenpos.menugroup.infra.MenuGroupRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ExistMenuGroupMenuCreateValidator implements MenuGroupCreateValidator {
    private static final String NOT_FOUND_ERROR_MESSAGE = "메뉴 그룹을 찾지 못하였습니다.";
    private final MenuGroupRepository menuGroupRepository;

    public ExistMenuGroupMenuCreateValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void validate(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new NotFoundMenuGroupException(NOT_FOUND_ERROR_MESSAGE);
        }
    }
}
