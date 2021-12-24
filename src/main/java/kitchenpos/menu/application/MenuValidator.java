package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroupRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void existMenuGroup(Long menuGroupId){
        if(!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }
}
