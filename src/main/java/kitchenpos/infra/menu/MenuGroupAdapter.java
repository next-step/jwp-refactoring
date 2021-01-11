package kitchenpos.infra.menu;

import kitchenpos.domain.menu.SafeMenuGroup;
import kitchenpos.domain.menu.exceptions.MenuGroupEntityNotFoundException;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupAdapter implements SafeMenuGroup {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupAdapter(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public void isExistMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuGroupEntityNotFoundException("존재하지 않은 메뉴 그룹으로 메뉴를 등록할 수 없습니다.");
        }
    }
}
