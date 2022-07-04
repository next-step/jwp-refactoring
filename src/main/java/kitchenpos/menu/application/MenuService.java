package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuDao;
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
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        addMenuProduct(menu, menuRequest);

        final Menu savedMenu = menuDao.save(menu);

        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuDao.findAll().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    private void addMenuProduct(Menu menu, MenuRequest menuRequest) {
        for(MenuProductRequest productRequest: menuRequest.getMenuProducts()) {
            final Product product = findProductById(productRequest.getProductId());

            menu.addMenuProduct(product, productRequest.getQuantity());
        }
    }

    private Product findProductById(Long productId) {
        return productDao.findById(productId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
