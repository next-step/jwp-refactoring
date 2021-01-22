package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuRepository menuRepository;

    public MenuService(MenuGroupService menuGroupService, ProductService productService, MenuRepository menuRepository) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
        List<Product> products = productService.findAllByIdIn(request.getProductsId());

        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
            .map(menuProduct -> new MenuProduct(getMatchProductInProducts(products, menuProduct), menuProduct.getQuantity()))
            .collect(Collectors.toList());
        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private Product getMatchProductInProducts(List<Product> products, MenuProductRequest menuProduct) {
        return products.stream()
            .filter(product -> product.getId().equals(menuProduct.getProductId()))
            .findFirst().get();
    }
}
