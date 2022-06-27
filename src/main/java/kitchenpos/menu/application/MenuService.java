package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.infrastructure.MenuGroupRepository;
import kitchenpos.menu.infrastructure.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.infrastructure.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuRepository menuRepository,
                       ProductRepository productRepository,
                       MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Price price = Price.from(menuRequest.getPrice());
        MenuGroup menuGroup = findMenuGroup(menuRequest);
        List<MenuProduct> menuProducts = createMenuProducts(menuRequest.getMenuProducts());
        Menu menu = Menu.createMenu(menuRequest.getName(), price, menuGroup, menuProducts);

        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProductRequests.forEach(menuProductRequest -> {
            Product product = findProduct(menuProductRequest);
            MenuProduct menuProduct = MenuProduct.createMenuProduct(product, menuProductRequest.getQuantity());
            menuProducts.add(menuProduct);
        });
        return menuProducts;
    }
    
    private Product findProduct(MenuProductRequest menuProductRequests) {
        return productRepository.findById(menuProductRequests.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    private MenuGroup findMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
