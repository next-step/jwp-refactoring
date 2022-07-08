package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.exception.MenuExceptionType;
import kitchenpos.menu.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final  MenuGroupRepository menuGroupRepository, final  MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest createMenuRequest) {
        menuValidator.validate(createMenuRequest);
        final MenuGroup menuGroup = findByMenuGroupId(createMenuRequest.getMenuGroupId());
        final Menu savedMenu = menuRepository.save(createMenuRequest.toEntity(menuGroup));
        savedMenu.addMenuProducts(createMenuRequest.toMenuProducts());

        return MenuResponse.of(savedMenu);
    }

    private MenuGroup findByMenuGroupId(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new MenuException(MenuExceptionType.MENU_GROUP_NOT_FOUND));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
