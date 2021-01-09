package kitchenpos.domain.menu;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.exceptions.MenuGroupEntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupAdapter implements SafeMenuGroup {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupAdapter(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Override
    public void isExistMenuGroup(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new MenuGroupEntityNotFoundException("존재하지 않은 메뉴 그룹으로 메뉴를 등록할 수 없습니다.");
        }
    }
}
