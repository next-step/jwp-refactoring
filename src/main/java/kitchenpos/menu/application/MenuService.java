package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository,
        MenuGroupService menuGroupService,
        ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        return MenuResponse.from(menuRepository.save(newMenu(request)));
    }

    public List<MenuResponse> list() {
        return MenuResponse.listFrom(menuRepository.findAll());
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    private Menu newMenu(MenuRequest request) {
        return Menu.of(
            request.name(),
            request.price(),
            menuGroup(request.getMenuGroupId()),
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
            product(request.getProductId()),
            request.quantity()
        );
    }

    private Product product(long productId) {
        return productService.findById(productId);
    }

    private MenuGroup menuGroup(long menuGroupId) {
        return menuGroupService.findById(menuGroupId);
    }
}
