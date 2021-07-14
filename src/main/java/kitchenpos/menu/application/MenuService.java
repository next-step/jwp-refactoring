package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import kitchenpos.menu.exception.NotFoundProductException;
import kitchenpos.wrap.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(() -> new NotFoundMenuGroupException());
        List<Product> products = findAllProductByIds(menuRequest.getProductIds());
        List<MenuProduct> menuProducts = toMenuProduct(products, menuRequest);
        Menu menu = toMenu(menuProducts, menuGroup, menuRequest);
        Menu persistMenu = menuRepository.save(menu);
        return MenuResponse.of(persistMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(menu -> MenuResponse.of(menu))
                .collect(Collectors.toList());
    }

    private List<Product> findAllProductByIds(List<Long> productIds) {
        List<Product> products = productRepository.findAllByIdIn(productIds);
        if (products.size() != productIds.size()) {
            throw new NotFoundProductException();
        }
        return products;
    }

    private Menu toMenu(List<MenuProduct> menuProductList, MenuGroup menuGroup, MenuRequest menuRequest) {
        MenuProducts menuProducts = new MenuProducts(menuProductList);
        Price price = new Price(menuRequest.getPrice());
        return new Menu(menuRequest.getName(), price, menuGroup, menuProducts);
    }

    private List<MenuProduct> toMenuProduct(List<Product> products, MenuRequest menuRequest) {
        return products.stream()
                .map(product -> findMenuProduct(product, menuRequest))
                .collect(Collectors.toList());
    }

    private MenuProduct findMenuProduct(Product product, MenuRequest menuRequest) {
        return menuRequest.getMenuProductRequests().stream()
                .filter(menuProductRequest -> menuProductRequest.getProductId() == product.getId())
                .map(menuProductRequest -> new MenuProduct(product, menuProductRequest.getQuantity()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
