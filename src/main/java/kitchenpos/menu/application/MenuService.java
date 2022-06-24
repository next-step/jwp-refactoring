package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroup(menuRequest);
        final List<MenuProduct> menuProducts = toMenuProduct(menuRequest.getMenuProducts());
        Menu menu = Menu.of(null, menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        if (!menuGroupRepository.existsById(menu.menuGroup().id())) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = findProduct(menuProduct);
            sum = sum.add(product.price().value().multiply(BigDecimal.valueOf(menuProduct.quantity().value())));
        }

        if (menu.price().value().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(MenuProducts.of(savedMenuProducts));
        return MenuResponse.of(savedMenu);
    }

    private Product findProduct(MenuProduct menuProduct) {
        return productRepository.findById(menuProduct.product().id())
                .orElseThrow(IllegalArgumentException::new);
    }

    private MenuGroup findMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(() -> new IllegalArgumentException("메뉴그룹을 찾을 수 없습니다."));
    }

    private List<MenuProduct> toMenuProduct(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = findProduct(menuProductRequest.getProductId());
            menuProducts.add(MenuProduct.of(null, null, product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private Product findProduct(long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("메뉴상품을 찾을 수 없습니다."));
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(MenuProducts.of(menuProductRepository.findAllByMenuId(menu.id())));
        }

        return menus;
    }
}
