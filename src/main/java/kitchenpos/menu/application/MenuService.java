package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupMenuValidator menuGroupMenuValidator;
    private final MenuValidator menuValidator;
    private final ProductMenuService productMenuService;

    @Autowired
    public MenuService(final MenuRepository menuRepository, final MenuGroupMenuValidator menuGroupMenuValidator,
                       final MenuValidator menuValidator, final ProductMenuService productMenuService) {
        this.menuRepository = menuRepository;
        this.menuGroupMenuValidator = menuGroupMenuValidator;
        this.menuValidator = menuValidator;
        this.productMenuService = productMenuService;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupMenuValidator.validateExistsMenuGroupById(menuRequest.getMenuGroupId());
        Menu menu = menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId()));
        menuValidator.validateMenuPrice(menu, getTotalProductsPrice(menuRequest));
        menuRequest.getMenuProductRequests()
                .forEach(menuProductRequest -> menu.addMenuProduct(createMenuProduct(menu, menuProductRequest)));
        return MenuResponse.of(menu);
    }

    private BigDecimal getTotalProductsPrice(MenuRequest menuRequest) {
        return menuRequest.getMenuProductRequests()
                .stream()
                .map(menuProductRequest -> productMenuService.calculateProductsPrice(menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuResponse> findAllMenu() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(Menu menu, MenuProductRequest menuProductRequest) {
        return new MenuProduct(menu, menuProductRequest.getProductId(), menuProductRequest.getQuantity());
    }
}
