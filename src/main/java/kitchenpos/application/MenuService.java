package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuGroup.MenuGroupRepository;
import kitchenpos.domain.menuProduct.MenuProduct;
import kitchenpos.domain.menuProduct.MenuProducts;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menuProduct.MenuProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());

        MenuProducts menuProducts = findMenuProducts(menuRequest.getMenuProducts());
        Menu menu = menuRequest.toMenu(menuGroup, menuProducts);

        menu.bindMenuProducts();

        List<Long> productIds = menu.getMenuProducts().findProductIds();
        extractedProducts(productIds);

        return MenuResponse.of(saveMenu(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = findMenus();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private void extractedProducts(List<Long> productIds) {
        if (!productRepository.existsAllByIdIn(productIds)) {
            throw new IllegalArgumentException("잘못된 상품 정보 입니다.");
        }
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(NoSuchElementException::new);
    }

    private Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    private List<Menu> findMenus() {
        return menuRepository.findAll();
    }

    private MenuProducts findMenuProducts(List<MenuProductRequest> menuProducts) {
        return new MenuProducts(menuProducts.stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList()));
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest) {
        Product product = findProduct(menuProductRequest.getProductId());
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(NoSuchElementException::new);
    }
}
