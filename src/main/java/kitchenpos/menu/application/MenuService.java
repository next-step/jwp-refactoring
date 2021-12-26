package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NoMenuException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product product = productService.findById(menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.fromList(menus);
    }

    public Menu findById(final Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(NoMenuException::new);
    }
}
