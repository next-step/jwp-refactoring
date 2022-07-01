package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository,
                       MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Products products = findProducts(menuRequest.getMenuProductIds());
        MenuProducts menuProducts = createMenuProducts(menuRequest.getMenuProducts(), products);
        Menu menu = createMenu(menuRequest, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu, products);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAllMenus() {
        List<MenuResponse> menuResponses = new ArrayList<>();
        final List<Menu> menus = menuRepository.findAll();
        for (Menu menu : menus){
            MenuProducts menuProducts = menu.getMenuProducts();
            Products products = findProducts(menuProducts.getProductIds());
            menuResponses.add(MenuResponse.of(menu, products));
        }
        return menuResponses;
    }

    private MenuProducts createMenuProducts(List<MenuProductRequest> menuProductRequests, Products products) {
        MenuProducts menuProducts = new MenuProducts();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final Quantity menuProductQuantity = new Quantity(menuProductRequest.getQuantity());
            final Product product = products.getProduct(menuProductRequest.getProductId());
            final MenuProduct menuProduct = new MenuProduct(menuProductQuantity, product.getId());
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    private Menu createMenu(MenuRequest menuRequest, MenuProducts menuProducts) {
        final Price price = new Price(menuRequest.getPrice());
        final MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());
        Menu menu = new Menu(menuRequest.getName(), price, menuGroup, menuProducts);
        menuValidator.validate(menu);
        return menu;
    }

    private Products findProducts(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        validateRequestProducts(products, productIds);
        return new Products(products);
    }

    private void validateRequestProducts(List<Product> products, List<Long> productIds) {
        if (products.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 등록된 상품이 없습니다.");
        }
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("[ERROR] 등록 되어있지 않은 상품이 있습니다.");
        }
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 메뉴 그룹이 등록되어있지 않습니다."));
    }

}
