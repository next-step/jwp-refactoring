package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator validator;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.validator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = getMenuGroup(request.getMenuGroupId());
        Menu menu = createMenu(request, menuGroup);
        validator.validate(menu);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private Menu createMenu(MenuRequest request, MenuGroup menuGroup) {
        return new Menu(request.getId(), request.getName(), request.getPrice(), menuGroup,
            createMenuProduct(request.getMenuProducts()));
    }

    private List<MenuProduct> createMenuProduct(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductRequest::toMenuProduct)
            .collect(Collectors.toList());
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 메뉴 그룹 입니다."));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
