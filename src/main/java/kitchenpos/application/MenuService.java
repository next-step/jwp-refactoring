package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupService menuGroupService;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupService menuGroupService
    ) {
        this.menuDao = menuDao;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public Menu create(final Menu menu) {

        MenuGroup menuGroup = menu.getMenuGroup();

        if (menuGroupService.isExists(menuGroup)) {
            throw new IllegalArgumentException("existed menuGroup");
        }

        if (menu.isReasonablePrice() == false) {
            throw new IllegalArgumentException("Total Price is higher then expected MenuProduct Price");
        }

        return menuDao.save(menu);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
