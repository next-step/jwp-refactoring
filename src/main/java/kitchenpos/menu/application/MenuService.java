package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository,
                       final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse createMenu(final MenuCreateRequest request) {
        menuValidator.validate(request);
        Menu menu = toMenu(request);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> findAllMenus() {
        return MenuResponse.list(menuRepository.findAllWithGroupAndProducts());
    }

    private Menu toMenu(MenuCreateRequest request) {
        return new Menu(
                request.getName(),
                request.toPrice(),
                request.getMenuGroupId(),
                toMenuProducts(request.getMenuProducts())
        );
    }

    private MenuProducts toMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(MenuProductRequest::toMenuProduct)
                .collect(Collectors.toList());
        return new MenuProducts(menuProducts);
    }
}
