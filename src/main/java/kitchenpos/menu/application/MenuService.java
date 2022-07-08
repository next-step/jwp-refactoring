package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuValidator menuValidator
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidator.validate(menuRequest);

        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuRequest.toMenuProducts());

        return MenuResponse.from(menuDao.save(menu));
    }

    public List<MenuResponse> list() {
        return menuDao.findAll().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    public Menu findMenuById(Long menuId) {
        return menuDao.findById(menuId)
            .orElseThrow(IllegalArgumentException::new);
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuDao.countByIdIn(menuIds);
    }
}
