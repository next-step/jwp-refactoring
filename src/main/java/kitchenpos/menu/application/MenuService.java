package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = menuRequest.getMenuProductRequests()
                .stream()
                .map(menuProductRequest -> menuProductRequest.toMenuProduct())
                .collect(Collectors.toList());

        final Menu menu = Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuProducts, menuValidator);
        menu.organizeMenu(menuProducts);
        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(menu -> MenuResponse.of(menu))
                .collect(Collectors.toList());
    }
}
