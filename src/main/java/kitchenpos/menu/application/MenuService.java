package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
        final MenuDao menuDao,
        final MenuGroupDao menuGroupDao,
        final MenuProductDao menuProductDao,
        final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        List<MenuProductRequest> menuProducts = request.getMenuProducts();
        Menu menu = request.toMenu(getMenuProducts(menuProducts));

        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(menu);

        return MenuResponse.from(savedMenu);
    }


    public List<MenuProduct> getMenuProducts(List<MenuProductRequest> requests) {
        return requests.stream()
            .map(menuProductRequest -> menuProductRequest
                .toMenuProduct(findProductById(menuProductRequest.getProductId())))
            .collect(Collectors.toList());
    }

    private Product findProductById(Long productId) {
        return productDao.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(""));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
