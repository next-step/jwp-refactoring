package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.product.domain.Menu;
import kitchenpos.product.domain.MenuGroup;
import kitchenpos.product.domain.MenuProduct;
import kitchenpos.product.domain.MenuRepository;
import kitchenpos.product.ui.request.MenuProductRequest;
import kitchenpos.product.ui.request.MenuRequest;
import kitchenpos.product.ui.response.MenuResponse;
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

    public Menu findById(long id) {
        return menuRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(
                String.format("메뉴 id(%d)를 찾을 수 없습니다.", id)));
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
