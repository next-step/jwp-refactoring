package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);

        List<MenuProduct> menuProducts = findMenuProducts(menuRequest);

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);

        return MenuResponse.from(menuDao.save(menu));
    }

    public List<MenuResponse> list() {
        return menuDao.findAll().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    public Menu findMenuById(Long menuId) {
        return menuDao.findById(menuId)
            .orElseThrow(IllegalArgumentException::new);
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuDao.countByIdIn(menuIds);
    }

    private List<MenuProduct> findMenuProducts(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for(MenuProductRequest productRequest: menuRequest.getMenuProducts()) {
            final Product product = findProductById(productRequest.getProductId());

            menuProducts.add(new MenuProduct(product, productRequest.getQuantity()));
        }

        return menuProducts;
    }

    private Product findProductById(Long productId) {
        return productDao.findById(productId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
