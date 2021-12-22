package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.dao.MenuDao;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;


    public MenuService(MenuDao menuDao, MenuGroupService menuGroupService,
        ProductService productService) {
        this.menuDao = menuDao;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId());
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();

        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productService.findProduct(menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup,
            menuProducts);
        Menu savedMenu = menuDao.save(menu);

        return MenuResponse.from(savedMenu);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }

    public List<MenuResponse> listResponse() {
        return list().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
