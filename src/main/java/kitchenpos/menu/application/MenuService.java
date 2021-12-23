package kitchenpos.menu.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            MenuDao menuDao
            , MenuGroupService menuGroupService
            , ProductService productService
    ) {
        this.menuDao = menuDao;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Name name = Name.of(request.getName());
        Price price = Price.of(request.getPrice());
        MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
        MenuProducts menuProducts = MenuProducts.of(toMenuProducts(request.getMenuProducts()));

        Menu menu = Menu.of(name, price, menuGroup, menuProducts);
        Menu persistMenu = menuDao.save(menu);
        return MenuResponse.of(persistMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> persistMenus = menuDao.findAll();

        return persistMenus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public Menu findById(Long menuId) {
        return menuDao.findById(menuId)
                .orElseThrow(NoSuchElementException::new);
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest ->
                {
                    Product product = productService.findById(menuProductRequest.getProductId());
                    long quantity = menuProductRequest.getQuantity();
                    return MenuProduct.of(product, quantity);
                })
                .collect(Collectors.toList());
    }
}
