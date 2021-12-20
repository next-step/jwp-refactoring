package kitchenpos.product.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.group.domain.MenuGroupQueryService;
import kitchenpos.product.menu.domain.Menu;
import kitchenpos.product.menu.domain.MenuCommandService;
import kitchenpos.product.menu.domain.MenuProduct;
import kitchenpos.product.menu.domain.MenuQueryService;
import kitchenpos.product.menu.ui.request.MenuProductRequest;
import kitchenpos.product.menu.ui.request.MenuRequest;
import kitchenpos.product.menu.ui.response.MenuResponse;
import kitchenpos.product.product.domain.ProductQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuCommandService menuCommandService;
    private final MenuQueryService menuQueryService;
    private final MenuGroupQueryService menuGroupQueryService;
    private final ProductQueryService productQueryService;

    public MenuService(MenuCommandService menuCommandService,
        MenuQueryService menuQueryService,
        MenuGroupQueryService menuGroupQueryService,
        ProductQueryService productQueryService) {
        this.menuCommandService = menuCommandService;
        this.menuQueryService = menuQueryService;
        this.menuGroupQueryService = menuGroupQueryService;
        this.productQueryService = productQueryService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        return MenuResponse.from(menuCommandService.save(newMenu(request)));
    }

    public List<MenuResponse> list() {
        return MenuResponse.listFrom(menuQueryService.findAll());
    }

    public List<MenuResponse> listByIds(List<Long> ids) {
        return MenuResponse.listFrom(menuQueryService.findAllById(ids));
    }

    private Menu newMenu(MenuRequest request) {
        return Menu.of(
            request.name(),
            request.price(),
            menuGroupQueryService.menuGroup(request.getMenuGroupId()),
            menuProducts(request.getMenuProducts())
        );
    }

    private List<MenuProduct> menuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(this::menuProduct)
            .collect(Collectors.toList());
    }

    private MenuProduct menuProduct(MenuProductRequest request) {
        return MenuProduct.of(
            productQueryService.product(request.getProductId()),
            request.quantity()
        );
    }
}
