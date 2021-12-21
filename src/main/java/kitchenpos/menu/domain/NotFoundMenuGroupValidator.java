package kitchenpos.menu.domain;

import kitchenpos.menu.infra.MenuGroupRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NotFoundMenuGroupValidator {
    private final MenuGroupRepository menuGroupRepository;

    public NotFoundMenuGroupValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void validate(long id) {
        if (!menuGroupRepository.existsById(id)) {
            throw new IllegalArgumentException("메뉴 그룹을 찾지 못하였습니다.");
        }
    }
}
