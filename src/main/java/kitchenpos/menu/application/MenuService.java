package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());

        MenuProducts menuProducts = findMenuProducts(menuRequest.getMenuProducts());
        Menu menu = menuRequest.toMenu(menuGroup, menuProducts);
        menuValidator.validateMenuProductsAmount(menu);

        return MenuResponse.of(saveMenu(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = findMenus();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    private List<Menu> findMenus() {
        return menuRepository.findAll();
    }

    private MenuProducts findMenuProducts(List<MenuProductRequest> menuProducts) {
        List<MenuProduct> resultMenuProducts = new ArrayList<>();
        menuProducts.forEach(
                menuProductRequest -> resultMenuProducts.add(menuProductRequest.toMenuProduct())
        );

        menuValidator.validateProducts(menuProducts);

        return new MenuProducts(resultMenuProducts);
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(NoSuchElementException::new);
    }
}
