package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductQuantity;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
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
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuProducts menuProducts = createMenuProducts(menuRequest.getMenuProducts());
        Menu menu = createMenu(menuRequest, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<MenuResponse> menuResponses = new ArrayList<>();
        final List<Menu> menus = menuRepository.findAll();
        for (Menu menu : menus) {
            MenuResponse menuResponse = MenuResponse.from(menu);
            menuResponses.add(menuResponse);
        }
        return menuResponses;
    }

    private MenuProducts createMenuProducts(List<MenuProductRequest> menuProductRequests) {
        MenuProducts menuProducts = new MenuProducts();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final MenuProductQuantity menuProductQuantity = new MenuProductQuantity(menuProductRequest.getQuantity());
            final Product product = findProduct(menuProductRequest.getProductId());
            final MenuProduct menuProduct = new MenuProduct(menuProductQuantity, product);
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    private Menu createMenu(MenuRequest menuRequest, MenuProducts menuProducts) {
        final MenuPrice menuPrice = new MenuPrice(menuRequest.getPrice());
        final MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());

        return new Menu(menuRequest.getName(), menuPrice, menuGroup, menuProducts);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 상품이 등록되어있지 않습니다."));
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 메뉴 그룹이 등록되어있지 않습니다."));
    }

}
