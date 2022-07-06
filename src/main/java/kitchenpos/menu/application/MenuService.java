package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    private final MenuGroupService menuGroupService;

    public MenuService(final MenuRepository menuRepository, final ProductRepository productRepository,
                       final MenuGroupService menuGroupService) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
        final MenuProducts menuProducts = createMenuProducts(request);

        Menu menu = new Menu.Builder()
                .name(request.getName())
                .price(request.getPrice())
                .menuGroup(menuGroup)
                .menuProducts(menuProducts)
                .build();

        return MenuResponse.from(menuRepository.save(menu));
    }

    private MenuProducts createMenuProducts(MenuRequest request) {
        MenuProducts menuProducts = new MenuProducts();
        for (final MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            final Product product = findProductById(menuProductRequest.getProductId());
            menuProducts.add(MenuProduct.of(product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }

    public Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("메뉴(%d)를 찾을 수 없습니다.", id)));
    }
}
