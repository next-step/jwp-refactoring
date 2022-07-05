package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.exception.MenuExceptionType;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductException;
import kitchenpos.product.exception.ProductExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
                       final MenuProductRepository menuProductRepository, final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest createMenuRequest) {
        final MenuGroup menuGroup = findByMenuGroupId(createMenuRequest.getMenuGroupId());
        final Menu savedMenu = menuRepository.save(createMenuRequest.toEntity(menuGroup));
        final List<MenuProduct> menuProducts = createMenuProducts(createMenuRequest);

        savedMenu.addMenuProducts(menuProducts);

        return MenuResponse.of(savedMenu);
    }

    private MenuGroup findByMenuGroupId(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new MenuException(MenuExceptionType.MENU_GROUP_NOT_FOUND));
    }

    private List<MenuProduct> createMenuProducts(final CreateMenuRequest createMenuRequest) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : createMenuRequest.getMenuProducts()) {
            final Product product = findByProduct(menuProductRequest.getProductId());
            menuProducts.add(MenuProduct.of(null, product, menuProductRequest.getQuantity()));
        }


        return menuProducts;
    }

    private Product findByProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return MenuResponse.ofList(menus);
    }
}
