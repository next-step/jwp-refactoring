package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.MenuRequest;
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
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup findMenuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());

        if (menuGroupService.isExists(findMenuGroup)) {
            throw new IllegalArgumentException("existed menuGroup");
        }

        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), findMenuGroup, menuRequest.getMenuProducts());

        if (menu.isReasonablePrice() == false) {
            throw new IllegalArgumentException("Total Price is higher then expected MenuProduct Price");
        }

        return menuDao.save(menu);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
