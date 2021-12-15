package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Menu;
import kitchenpos.product.domain.MenuGroupRepository;
import kitchenpos.product.domain.MenuProduct;
import kitchenpos.product.domain.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.ui.request.MenuProductRequest;
import kitchenpos.product.ui.request.MenuRequest;
import kitchenpos.product.ui.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository,
        MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        return MenuResponse.from(menuRepository.save(newMenu(request)));
    }

    public List<MenuResponse> list() {
        return MenuResponse.listFrom(menuRepository.findAll());
    }

    private Menu newMenu(MenuRequest request) {
        return Menu.of(
            request.name(),
            request.price(),
            menuGroupRepository.menuGroup(request.getMenuGroupId()),
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
            productRepository.product(request.getProductId()),
            request.quantity()
        );
    }
}
