package kitchenpos.infra.menu;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.SafeMenuGroup;
import kitchenpos.domain.menu.exceptions.MenuGroupEntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupAdapter implements SafeMenuGroup {
    private final MenuGroupService menuGroupService;

    public MenuGroupAdapter(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @Override
    public void isExistMenuGroup(final Long menuGroupId) {
        if (!menuGroupService.existsById(menuGroupId)) {
            throw new MenuGroupEntityNotFoundException("존재하지 않은 메뉴 그룹으로 메뉴를 등록할 수 없습니다.");
        }
    }
}
