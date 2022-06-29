package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        menuValidator.validate(request);
        Menu menu = request.toMenu();
        List<MenuProduct> menuProducts = makeMenuProductsForAdding(request, menu);
        menu.addMenuProduct(menuProducts);
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> makeMenuProductsForAdding(MenuRequest request, Menu menu) {
        List<MenuProductRequest> menuProducts = request.getMenuProducts();
        return menuProducts.stream().map(menuProduct ->
            new MenuProduct(
                menu,
                menuProduct.getProductId(),
                menuProduct.getQuantity())
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }
}
