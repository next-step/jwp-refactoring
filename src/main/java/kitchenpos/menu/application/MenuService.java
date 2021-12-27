package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NoMenuException;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.NoProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuGroupService menuGroupService;

    private final ProductService productService;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupService menuGroupService, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        MenuProducts menuProducts = getMenuProducts(menuRequest);
        Menu menu = new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup, menuProducts);
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private MenuProducts getMenuProducts(final MenuRequest menuRequest) {
        MenuProducts menuProducts = new MenuProducts();
        List<Product> products = productService.findAllById(menuRequest.getMenuProducts()
                .stream()
                .map(MenuProductRequest::getProductId).collect(Collectors.toList()));
        getMenuProduct(menuRequest, menuProducts, products);
        return menuProducts;
    }

    private void getMenuProduct(MenuRequest menuRequest, MenuProducts menuProducts, List<Product> products) {
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product filteredProduct = getFilteredProduct(products, menuProductRequest);
            menuProducts.add(new MenuProduct(filteredProduct, menuProductRequest.getQuantity()));
        }
    }

    private Product getFilteredProduct(List<Product> products, MenuProductRequest menuProductRequest) {
        return products.stream()
                .filter(product -> Objects.equals(product.getId(), menuProductRequest.getProductId()))
                .findFirst()
                .orElseThrow(NoProductException::new);
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.fromList(menus);
    }

    public Menu findById(final Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(NoMenuException::new);
    }

    public List<Menu> findAllById(List<Long> menuIds) {
        return menuRepository.findAllById(menuIds);
    }
}
